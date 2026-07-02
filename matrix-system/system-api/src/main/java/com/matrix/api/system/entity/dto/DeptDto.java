package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 部门创建/修改 DTO */
@Data
@Schema(description = "部门DTO")
public class DeptDto {
    private Long id;
    private Long parentId;
    @Schema(description = "部门名称")
    private String name;
    private String leader;
    private String phone;
    private String email;
    private Integer sort;
    private Integer status;
}
