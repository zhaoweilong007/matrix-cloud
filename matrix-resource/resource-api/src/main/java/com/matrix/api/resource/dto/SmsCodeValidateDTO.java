package com.matrix.api.resource.dto;

import com.matrix.api.resource.sms.SmsSceneEnum;
import com.matrix.validator.annotation.InEnum;
import com.matrix.validator.annotation.PhoneValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "短信验证码的校验")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsCodeValidateDTO {

    @Schema(description = "手机号")
    @PhoneValue
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "发送场景")
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;

    @Schema(description = "验证码")
    @NotEmpty(message = "验证码不能为空")
    private String code;

}