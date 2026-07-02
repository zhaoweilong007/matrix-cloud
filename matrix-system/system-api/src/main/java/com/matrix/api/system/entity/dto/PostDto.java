package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 岗位创建/修改 DTO */
@Data
@Schema(description = "岗位DTO")
public class PostDto {
    private Long id;
    @Schema(description = "岗位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    @Schema(description = "岗位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private Long deptId;
    private String postCategory;
    private Integer sort;
    private Integer status;
    private String remark;
}
