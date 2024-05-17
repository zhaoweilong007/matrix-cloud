package com.matrix.api.resource.dto;

import com.matrix.api.resource.sms.SmsSceneEnum;
import com.matrix.validator.annotation.InEnum;
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
public class BusinessSmsCodeValidateDTO {

    @Schema(description = "业务ID(房源ID)")
    @NotNull(message = "业务ID(房源ID)不能为空")
    private Long businessId;

    @Schema(description = "发送场景")
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;

    @Schema(description = "验证码")
    @NotEmpty(message = "验证码")
    private String code;

}