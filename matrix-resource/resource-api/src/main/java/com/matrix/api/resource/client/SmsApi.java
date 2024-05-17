package com.matrix.api.resource.client;

import com.matrix.api.resource.dto.BusinessSmsCodeValidateDTO;
import com.matrix.api.resource.dto.SmsCodeSendDTO;
import com.matrix.api.resource.dto.SmsCodeValidateDTO;
import com.matrix.api.resource.dto.SmsDto;
import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 短信发送
 *
 * @author LeonZhou
 * @since 2023/6/20
 */
@FeignClient(contextId = "SmsApi", value = ServerNameConstants.RESOURCE, path = SmsApi.PREFIX)
public interface SmsApi {

    String PREFIX = "/sms";

    /**
     * 短信发送
     *
     * @param smsVo 短信vo
     * @return 。
     */
    @Operation(summary = "发送短信", description = "短信发送")
    @PostMapping("/sendMsg")
    R<Void> sendMsg(@Valid @RequestBody SmsDto smsVo);


    /**
     * 短信发送
     *
     * @param smsVo 短信vo
     * @return 。
     */
    @Operation(summary = "异步发送短信", description = "异步发送短信")
    @PostMapping("/sendMsgAsync")
    void sendMsgAsync(@Valid @RequestBody SmsDto smsVo);


    /**
     * 验证码发送
     *
     * @param smsCodeSendDTO 短信vo
     * @return 。
     */
    @Operation(summary = "发送短信验证码", description = "验证码发送")
    @PostMapping("/sendSmsCode")
    R<Void> sendSmsCode(@Valid @RequestBody SmsCodeSendDTO smsCodeSendDTO);


    /**
     * 验证短信码
     *
     * @param reqDTO 验证对象
     * @return {@link R}<{@link Boolean}>
     */
    @Operation(summary = "检查验证码是否有效", description = "验证短信码")
    @PostMapping("/validate")
    R<Boolean> validateSmsCode(@Valid @RequestBody SmsCodeValidateDTO reqDTO);


    /**
     * 验证短信码-根据业务ID
     *
     * @param reqDTO 验证对象
     * @return {@link R}<{@link Boolean}>
     */
    @Operation(summary = "检查验证码是否有效", description = "验证短信码")
    @PostMapping("/validateSmsCodeByBusinessId")
    R<Boolean> validateSmsCodeByBusinessId(@Valid @RequestBody BusinessSmsCodeValidateDTO reqDTO);
}
