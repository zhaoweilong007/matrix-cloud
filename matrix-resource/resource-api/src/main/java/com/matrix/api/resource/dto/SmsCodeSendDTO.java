package com.matrix.api.resource.dto;

import com.matrix.api.resource.sms.SmsSceneEnum;
import com.matrix.validator.annotation.PhoneValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码发送
 *
 * @author ZhaoWeiLong
 * @since 2023/7/3
 **/
@Data
@Schema(name = "SmsCodeSendDTO", description = "短信验证码发送")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsCodeSendDTO {

    @NotBlank(message = "电话号码(phone)不能为空")
    @PhoneValue
    private String phone;

    @Schema(description = "发送场景")
    @NotNull(message = "发送场景不能为空")
    private SmsSceneEnum scene;
}
