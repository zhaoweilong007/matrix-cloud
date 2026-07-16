package com.matrix.jpush;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.jiguang.common.DeviceType;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.jpush.domain.PushObject;
import com.matrix.jpush.properties.JPushProperties;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 极光推送操作模板，封装推送、别名管理、手机号解密等核心操作
 **/
@Slf4j
public class JPushTemplate {

    private final JPushClient jPushClient;
    private final JPushProperties properties;
    private final PrivateKey privateKey;

    /**
     * 根据配置创建 JPush 模板
     *
     * @param properties 极光推送配置
     */
    public JPushTemplate(JPushProperties properties) {
        this.properties = properties;
        this.jPushClient = new JPushClient(properties.getMasterSecret(), properties.getAppKey());
        try {
            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(properties.getPrivateKey()));
            this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("极光初始化RSA私钥异常", e);
            throw new RuntimeException("极光初始化RSA私钥异常", e);
        }
    }

    /**
     * 向所有设备推送消息
     *
     * @param pushObject 推送内容
     * @return 是否推送成功
     */
    public boolean sendPush(PushObject pushObject) {
        return this.sendPush(Audience.all(), pushObject);
    }

    /**
     * 根据别名列表推送消息
     *
     * @param alias      别名列表
     * @param pushObject 推送内容
     * @return 是否推送成功
     */
    public boolean sendPush(List<String> alias, PushObject pushObject) {
        return this.sendPush(Audience.alias(alias), pushObject);
    }

    /**
     * 根据标签列表推送消息
     *
     * @param tags       标签列表
     * @param pushObject 推送内容
     * @return 是否推送成功
     */
    public boolean sendPushByTag(List<String> tags, PushObject pushObject) {
        return this.sendPush(Audience.tag(tags), pushObject);
    }

    /**
     * 向指定受众推送消息
     *
     * @param audience   推送目标
     * @param pushObject 推送内容
     * @return 是否推送成功
     */
    public boolean sendPush(Audience audience, PushObject pushObject) {
        PushPayload payload = JPushNotifications.buildPushPayloadForAndroidAndIos(
                properties.getApnsProduction(), audience, pushObject);
        try {
            PushResult result = jPushClient.sendPush(payload);
            log.debug("sendPush result:{}", result);
            return result.isResultOK();
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            log.warn("sendPush error:", e);
        }
        return false;
    }

    /**
     * 清除指定别名
     *
     * @param alias 别名
     */
    public void clearAlias(String alias) {
        try {
            jPushClient.deleteAlias(alias, DeviceType.Android.value());
            jPushClient.deleteAlias(alias, DeviceType.IOS.value());
        } catch (APIConnectionException | APIRequestException e) {
            log.error("清理Alias异常", e);
        }
    }

    /**
     * 通过token获取手机号
     *
     * @param token 令牌
     */
    public String getPhoneNumberByToken(String token) {
        Assert.notNull(token);
        JSONObject object = new JSONObject();
        object.put("loginToken", token);
        final HttpResponse response = HttpRequest.post(properties.getLoginTokenUrl())
                .contentType(ContentType.JSON.getValue())
                .basicAuth(properties.getAppKey(), properties.getMasterSecret())
                .body(object.toJSONString())
                .execute();
        if (response.getStatus() != HttpStatus.HTTP_OK) {
            log.error("极光推送调用失败：{}", response);
            throw new ServiceException(SystemErrorTypeEnum.SYSTEM_ERROR);
        }
        final String body = response.body();
        log.debug("jpush execute loginTokenUrl result body:{}", body);
        final JSONObject result = JSON.parseObject(body);
        if (result.getInteger("code") != 8000) {
            log.error("极光推送调用失败：{}", result);
            throw new ServiceException(BusinessErrorTypeEnum.JPUSH_TOKEN_INVALID);
        }
        String jgphone = result.getString("phone");
        return decrypt(jgphone);
    }

    /**
     * RSA 解密手机号
     *
     * @param phone 加密的手机号
     * @return 解密后的手机号
     */
    public String decrypt(String phone) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] b = Base64.getDecoder().decode(phone);
            return new String(cipher.doFinal(b));
        } catch (Exception e) {
            log.error("极光解密数据异常", e);
            throw new ServiceException(SystemErrorTypeEnum.SYSTEM_ERROR);
        }
    }
}
