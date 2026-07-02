package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 租户创建/修改 DTO。
 */
@Data
@Schema(description = "租户DTO")
public class TenantDto {

    @NotBlank(message = "租户名称不能为空")
    @Schema(description = "租户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tenantName;

    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    @Schema(description = "关联套餐 ID")
    private Long packageId;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactMobile;

    @Schema(description = "到期时间")
    private java.time.LocalDateTime expireTime;

    @Schema(description = "最大账号数")
    private Integer accountCount;
}
