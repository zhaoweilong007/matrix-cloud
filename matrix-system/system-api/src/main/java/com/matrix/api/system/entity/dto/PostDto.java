package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 岗位创建/修改 DTO */
@Data
@Schema(description = "岗位DTO")
public class PostDto {
    private Long id;

    @NotBlank(message = "岗位编码不能为空")
    @Schema(description = "岗位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "岗位名称不能为空")
    @Schema(description = "岗位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private Long deptId;
    private String postCategory;
    private Integer sort;
    private Integer status;
    private String remark;
}
