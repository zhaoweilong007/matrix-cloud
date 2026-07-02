package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 租户套餐创建/修改 DTO */
@Data
@Schema(description = "租户套餐DTO")
public class TenantPackageDto {
    private Long id;

    @NotBlank(message = "套餐名称不能为空")
    @Schema(description = "套餐名称")
    private String name;
    private Integer status;
    private String menuIds;
    private String remark;
}
