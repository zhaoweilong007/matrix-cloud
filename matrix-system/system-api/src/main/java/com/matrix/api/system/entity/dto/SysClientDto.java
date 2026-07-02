package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** OAuth2 客户端 DTO */
@Data
@Schema(description = "客户端DTO")
public class SysClientDto {
    private Long id;

    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @NotBlank(message = "客户端密钥不能为空")
    private String clientKey;
    private String clientSecret;
    private String grantTypes;
    private String deviceType;
    private Long activeTimeout;
    private Long timeout;
    private Integer status;
    private String remark;
}
