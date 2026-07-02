package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 资源分类创建/修改 DTO。
 */
@Data
@Schema(description = "资源分类DTO")
public class SysResourceCategoryDto {

    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;
}
