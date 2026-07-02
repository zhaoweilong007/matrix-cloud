package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 租户创建/修改 DTO。
 */
@Data
@Schema(description = "租户DTO")
public class TenantDto {

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    @Schema(description = "关联套餐 ID")
    private Long packageId;
}
