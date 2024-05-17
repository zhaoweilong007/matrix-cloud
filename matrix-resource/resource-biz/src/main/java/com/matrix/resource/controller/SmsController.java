package com.matrix.resource.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.matrix.api.resource.client.SmsApi;
import com.matrix.api.resource.dto.BusinessSmsCodeValidateDTO;
import com.matrix.api.resource.dto.SmsCodeSendDTO;
import com.matrix.api.resource.dto.SmsCodeValidateDTO;
import com.matrix.api.resource.dto.SmsDto;
import com.matrix.api.resource.sms.SmsSceneEnum;
import com.matrix.api.resource.sms.SmsSignNameEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.common.util.ProfileUtils;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.sms.enums.SmsBlendEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = SmsApi.PREFIX)
@Tag(name = "短信服务", description = "短信服务")
@Slf4j
@Validated
@RequiredArgsConstructor
public class SmsController implements SmsApi {


    @Override
    public R<Void> sendMsg(SmsDto smsDto) {
        if (smsDto.getParams() == null) {
            smsDto.setParams(new LinkedHashMap<>());
        }
        final SmsBlendEnum smsBlendEnum = SmsBlendEnum.ofName(smsDto.getScene().getSignNameEnum().name());
        final String name = smsBlendEnum.name();
        SmsBlend smsBlend = SmsFactory.getSmsBlend(name);
        log.info("smsSendReq: smsBlend:{} phone:{} scene:{} params:{}", name, smsDto.getPhone(), smsDto.getScene(), smsDto.getParams());
        SmsResponse smsResponse = smsBlend.sendMessage(smsDto.getPhone(), smsDto.getScene().getTemplateId(), smsDto.getParams());
        log.info("smsResponse：{}", smsResponse);
        final boolean sentSms = smsResponse.isSuccess();
        return sentSms ? R.success() : R.fail(SystemErrorTypeEnum.SMS_SEND_FAIL);
    }

    @Override
    public void sendMsgAsync(SmsDto smsVo) {
        CompletableFuture.runAsync(() -> sendMsg(smsVo));
    }

    @Override
    public R<Void> sendSmsCode(SmsCodeSendDTO smsCodeSendDTO) {
        final SmsSceneEnum sceneEnum = smsCodeSendDTO.getScene();
        // 创建验证码
        final String code = RandomUtil.randomString("123456789", 6);
        String key = sceneEnum.getSmsCodeKey(smsCodeSendDTO.getPhone());
        final boolean existsObject = RedisUtils.isExistsObject(key);
        final long time = RedisUtils.remainTimeToLive(key);
        if (existsObject && time >= 9 * 60 * 1000) {
            return R.fail(SystemErrorTypeEnum.SMS_HAS_SEND);
        }
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("code", code);
        final SmsSignNameEnum smsSignNameEnum = smsCodeSendDTO.getScene().getSignNameEnum();
        SmsBlend smsBlend = SmsFactory.getSmsBlend(smsSignNameEnum.name());
        SmsResponse smsResponse = smsBlend.sendMessage(smsCodeSendDTO.getPhone(), sceneEnum.getTemplateId(), params);
        log.info("smsResponse：{}", smsResponse);
        final boolean sentSms = smsResponse.isSuccess();
        if (sentSms) {
            // 有效期 十分钟
            RedisUtils.setCacheObject(key, code, Duration.ofMinutes(10));
            return R.success();
        }
        log.info("smsResponse：{}", smsResponse);
        return R.fail(SystemErrorTypeEnum.SMS_SEND_FAIL);
    }

    @Override
    public R<Boolean> validateSmsCode(SmsCodeValidateDTO reqDTO) {
        log.info("reqDTO：{}", reqDTO);
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(sceneEnum, "验证码场景({}) 查找不到配置", reqDTO.getScene());
        String key = sceneEnum.getSmsCodeKey(reqDTO.getMobile());
        if (ProfileUtils.isTest()) {
            return R.success();
        }
        String code = RedisUtils.getCacheObject(key);
        final boolean equals = Objects.equals(code, reqDTO.getCode());
        if (equals) {
            RedisUtils.deleteObject(key);
        }
        return equals ? R.success() : R.fail(SystemErrorTypeEnum.SMS_VALIDATOR_FAIL);
    }


    @Override
    public R<Boolean> validateSmsCodeByBusinessId(BusinessSmsCodeValidateDTO reqDTO) {
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(sceneEnum, "验证码场景({}) 查找不到配置", reqDTO.getScene());
        String key = sceneEnum.getSmsCodeKey(reqDTO.getBusinessId().toString());
        if (ProfileUtils.isTest()) {
            RedisUtils.deleteObject(key);
            return R.success();
        }
        String code = RedisUtils.getCacheObject(key);
        final boolean equals = Objects.equals(code, reqDTO.getCode());
        if (equals) {
            RedisUtils.deleteObject(key);
        }
        return equals ? R.success() : R.fail(SystemErrorTypeEnum.SMS_VALIDATOR_FAIL);
    }
}
