package com.matrix.jpush.utils;

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
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.util.SpringUtils;
import com.matrix.jpush.config.properties.JPushProperties;
import com.matrix.jpush.domain.JPushVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeonZhou
 * @since 2023/6/20
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JPushUtil {

    private static final int RESPONSE_OK = 200;
    private static JPushProperties jPushProperties = SpringUtils.getBean(JPushProperties.class);
    private static JPushClient jPushClient;

    public static JPushClient getJPushClient() {
        if (jPushClient == null) {
            jPushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey());
        }
        return jPushClient;
    }


    /**
     * 通过token获取手机号
     *
     * @param token
     * @return {@link String}
     * @throws Exception
     */
    public static String getPhoneNumberByToken(String token) {
        Assert.notNull(token);
        JSONObject object = new JSONObject();
        object.put("loginToken", token);

        final HttpResponse response = HttpRequest.post(jPushProperties.getLoginTokenUrl())
                .contentType(ContentType.JSON.getValue())
                .basicAuth(jPushProperties.getAppKey(), jPushProperties.getMasterSecret())
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


    public static String decrypt(String phone) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(jPushProperties.getPrivateKey()));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] b = Base64.getDecoder().decode(phone);
            return new String(cipher.doFinal(b));
        } catch (Exception e) {
            log.error("极光解密数据异常", e);
            throw new ServiceException(SystemErrorTypeEnum.SYSTEM_ERROR);
        }
    }


    //极光推送>>All所有平台
    public static void sendToAll(JPushVo vo) {
        //创建JPushClient
        JPushClient jpushClient = getJPushClient();
        PushPayload.Builder builder = createBuilder(vo);
        PushPayload payload = builder.build();
        try {
            log.info("[极光推送]PushPayload：{}", payload);
            PushResult pushResult = jpushClient.sendPush(payload);
            log.info("[极光推送]PushResult：{}", pushResult);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    private static PushPayload.Builder createBuilder(JPushVo vo) {
        PushPayload.Builder builder = PushPayload.newBuilder();
        Map<String, String> extras = null;
        try {
            extras = BeanUtils.describe(vo);
        } catch (Exception e) {
            log.info("BeanUtils.describe转化失败,{}", e);
        }
        //创建option
        if (vo.getPlatform() == null) {
            builder.setPlatform(Platform.all())  //所有平台的用户
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(IosNotification.newBuilder() //发送ios
                                    .setAlert(vo.getMsg()) //消息体
                                    .setBadge(+1)
                                    .setSound("default") //ios提示音
                                    .addExtras(extras) //附加参数
                                    .build())
                            .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                                    .addExtras(extras) //附加参数
                                    .setAlert(vo.getMsg()) //消息体
                                    .setTitle(vo.getTitle())
                                    .build())
                            .build());
        } else if (DeviceType.Android.value().equals(vo.getPlatform())) {
            builder.setPlatform(Platform.android())
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                                    .addExtras(extras) //附加参数
                                    .setAlert(vo.getMsg()) //消息体
                                    .setTitle(vo.getTitle())
                                    .build())
                            .build());
        } else if (DeviceType.IOS.value().equals(vo.getPlatform())) {
            builder.setPlatform(Platform.ios())
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(IosNotification.newBuilder() //发送ios
                                    .addExtras(extras) //附加参数
                                    .setAlert(vo.getMsg()) //消息体
                                    .setBadge(+1)
                                    .setSound("default") //ios提示音
                                    .build())
                            .build());
        }
        builder.setAudience(Audience.alias(vo.getAlias()))
                .setOptions(Options.newBuilder().setApnsProduction(jPushProperties.getApnsProduction()).build())//指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式)
                .setMessage(Message.newBuilder().setMsgContent(vo.getMsg()).addExtras(extras).build());//自定义信息
        return builder;
    }

    /**
     * 按指定账号推送一条消息(IOS)
     *
     * @param title    标题
     * @param param    消息内容
     * @param deviceNo 设备编号
     */
    public static PushResult pushSingleIos(String title, Map<String, String> param, String deviceNo) {
        JPushClient jpushClient = getJPushClient();
        try {
            IosNotification iosNotification = IosNotification.newBuilder()
                    .setSound("default")
                    .addExtras(param)
                    .setAlert(title)
                    .build();
            PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.ios())
                    .setAudience(Audience.registrationId(deviceNo))
                    .setNotification(Notification.newBuilder().addPlatformNotification(iosNotification).build())
                    .setOptions(Options.newBuilder()
                            .setTimeToLive(0)
                            .build())
                    .build();
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.info("推送一条IOS消息失败", e);
        } catch (APIRequestException e) {
            log.info("推送一条IOS消息失败: " + e.getErrorMessage());
        }
        return null;
    }

    /**
     * 按指定账号推送一条消息(Android)
     *
     * @param title      标题
     * @param msgContent 消息内容
     * @param deviceNo   设备编号
     */
    public static PushResult pushSingleAndroid(String title, String msgContent, Map<String, String> param, String deviceNo) {
        JPushClient jpushClient = getJPushClient();
        try {
            JsonObject obj = new JsonObject();
            obj.add("url", new JsonPrimitive("intent:#Intent;action=com.fangdx.xinfangtong.common.im.activity.OpenClickActivity;component=com.fangdx.xinfangtong/com.fangdx.xinfangtong.common.im.activity.OpenClickActivity;end"));
            Map<String, String> uriActivityMap = new HashMap<>();
            uriActivityMap.put("uri_activity", "com.fangdx.xinfangtong.common.im.activity.OpenClickActivity");
            Map<String, String> uriActionMap = new HashMap<>();
            uriActionMap.put("uri_action", "com.fangdx.xinfangtong.common.im.activity.OpenClickActivity");
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.registrationId(deviceNo))
                    .setNotification(new Notification.Builder()
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setAlert(msgContent)
                                    .setTitle(title)
                                    .addExtras(param)
                                    .setIntent(obj)
                                    .addCustom(uriActivityMap)
                                    .addCustom(uriActionMap)
                                    .build())
                            .build())
                    .setOptions(Options.newBuilder()
                            .setTimeToLive(0)
                            .build()).build();
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.info("推送一条Android消息失败", e);
        } catch (APIRequestException e) {
            log.info("推送一条Android消息失败: " + e.getErrorMessage());
        }
        return null;
    }

    /**
     * 推送到alias列表
     *
     * @param alias             别名或别名组
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extras            扩展字段
     */
    public void sendToAliasList(List<String> alias, String notificationTitle, String msgTitle, String msgContent, String extras) {
        PushPayload pushPayload = buildPushObject_all_aliasList_alertWithTitle(alias, notificationTitle, msgTitle, msgContent, extras);
        this.sendPush(pushPayload);
    }

    /**
     * 推送到taerg列表
     *
     * @param tagsList          Tag或Tag组
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extras            扩展字段
     */
    public void sendToTagsList(List<String> tagsList, String notificationTitle, String msgTitle, String msgContent, String extras) {
        PushPayload pushPayload = buildPushObject_all_tagList_alertWithTitle(tagsList, notificationTitle, msgTitle, msgContent, extras);
        this.sendPush(pushPayload);
    }

    /**
     * 发送给所有安卓用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extras            扩展字段
     */
    public void sendToAllAndroid(String notificationTitle, String msgTitle, String msgContent, String extras) {
        PushPayload pushPayload = buildPushObject_android_all_alertWithTitle(notificationTitle, msgTitle, msgContent, extras);
        this.sendPush(pushPayload);
    }

    /**
     * 发送给所有IOS用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extras            扩展字段
     */
    public void sendToAllIOS(String notificationTitle, String msgTitle, String msgContent, String extras) {
        PushPayload pushPayload = buildPushObject_ios_all_alertWithTitle(notificationTitle, msgTitle, msgContent, extras);
        this.sendPush(pushPayload);
    }

    /**
     * 发送给所有用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extras            扩展字段
     */
    public void sendToAll(String notificationTitle, String msgTitle, String msgContent, String extras) {
        PushPayload pushPayload = buildPushObject_android_and_ios(notificationTitle, msgTitle, msgContent, extras);
        this.sendPush(pushPayload);
    }

    private PushResult sendPush(PushPayload pushPayload) {
        log.info("pushPayload={}", pushPayload);
        PushResult pushResult = null;
        try {
            pushResult = this.getJPushClient().sendPush(pushPayload);
            log.info("" + pushResult);
            if (pushResult.getResponseCode() == RESPONSE_OK) {
                log.info("push successful, pushPayload={}", pushPayload);
            }
        } catch (APIConnectionException e) {
            log.error("push failed: pushPayload={}, exception={}", pushPayload, e);
        } catch (APIRequestException e) {
            log.error("push failed: pushPayload={}, exception={}", pushPayload, e);
        }

        return pushResult;
    }

    /**
     * 向所有平台所有用户推送消息
     *
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extras
     * @return
     */
    public PushPayload buildPushObject_android_and_ios(String notificationTitle, String msgTitle, String msgContent, String extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(notificationTitle)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationTitle)
                                .setTitle(notificationTitle)
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("androidNotification extras key", extras)
                                .build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()
                                // 传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                // 直接传alert
                                // 此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("iosNotification extras key", extras)
                                // 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)
                                .build()
                        )
                        .build()
                )
                // Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extras)
                        .build())
                .setOptions(Options.newBuilder()
                        // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        //.setApnsProduction(jPushProperties.getApnsProduction())
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }

    /**
     * 向所有平台单个或多个指定别名用户推送消息
     *
     * @param aliasList
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extras
     * @return
     */
    private PushPayload buildPushObject_all_aliasList_alertWithTitle(List<String> aliasList, String notificationTitle, String msgTitle, String msgContent, String extras) {
        // 创建一个IosAlert对象，可指定APNs的alert、title等字段
        // IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

        return PushPayload.newBuilder()
                // 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                // 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(aliasList))
                // jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        // 指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationTitle)
                                .setTitle(notificationTitle)
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("androidNotification extras key", extras)
                                .build())
                        // 指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                // 传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                // 直接传alert
                                // 此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("iosNotification extras key", extras)
                                // 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // 取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build())
                        .build())
                // Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extras)
                        .build())
                .setOptions(Options.newBuilder()
                        // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        //.setApnsProduction(jPushProperties.getApnsProduction())
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)
                        .build())
                .build();

    }

    /**
     * 向所有平台单个或多个指定Tag用户推送消息
     *
     * @param tagsList
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extras
     * @return
     */
    private PushPayload buildPushObject_all_tagList_alertWithTitle(List<String> tagsList, String notificationTitle, String msgTitle, String msgContent, String extras) {
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

        return PushPayload.newBuilder()
                // 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                // 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.tag(tagsList))
                // jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        // 指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationTitle)
                                .setTitle(notificationTitle)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("androidNotification extras key", extras)
                                .build())
                        // 指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                // 传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                // 直接传alert
                                // 此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("iosNotification extras key", extras)
                                // 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // 取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build())
                        .build())
                // Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extras)
                        .build())
                .setOptions(Options.newBuilder()
                        // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(jPushProperties.getApnsProduction())
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)
                        .build())
                .build();

    }

    /**
     * 向android平台所有用户推送消息
     *
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extras
     * @return
     */
    private PushPayload buildPushObject_android_all_alertWithTitle(String notificationTitle, String msgTitle, String msgContent, String extras) {
        return PushPayload.newBuilder()
                // 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                // 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                // jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        // 指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationTitle)
                                .setTitle(notificationTitle)
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("androidNotification extras key", extras)
                                .build())
                        .build()
                )
                // Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extras)
                        .build())

                .setOptions(Options.newBuilder()
                        // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(jPushProperties.getApnsProduction())
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }

    /**
     * 向ios平台所有用户推送消息
     *
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extras
     * @return
     */
    private PushPayload buildPushObject_ios_all_alertWithTitle(String notificationTitle, String msgTitle, String msgContent, String extras) {
        return PushPayload.newBuilder()
                // 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.ios())
                // 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                // jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        // 指定当前推送的android通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                // 传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                // 直接传alert
                                // 此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("iosNotification extras key", extras)
                                // 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)
                                .build())
                        .build()
                )
                // Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extras)
                        .build())
                .setOptions(Options.newBuilder()
                        // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        //.setApnsProduction(jPushProperties.getApnsProduction())
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }
}