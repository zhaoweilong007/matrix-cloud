package com.matrix.api.resource.dto;

import com.matrix.api.resource.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Schema(description = "短信vo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDto {


    /**
     * 电话
     */
    @Schema(description = "电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "电话号码(phone)不能为空")
    private String phone;


    @Schema(description = "发送场景", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发送场景不能为空")
    private SmsSceneEnum scene;

    /**
     * 短信内容(params)
     */
    @Schema(description = "短信内容(params)")
    private LinkedHashMap<String, String> params;
}
