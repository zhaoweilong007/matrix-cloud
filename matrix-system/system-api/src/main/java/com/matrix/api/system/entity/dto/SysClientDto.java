package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** OAuth2 客户端 DTO */
@Data
@Schema(description = "客户端DTO")
public class SysClientDto {
    private Long id;
    private String clientId;
    private String clientKey;
    private String clientSecret;
    private String grantTypes;
    private String deviceType;
    private Long activeTimeout;
    private Long timeout;
    private Integer status;
    private String remark;
}
