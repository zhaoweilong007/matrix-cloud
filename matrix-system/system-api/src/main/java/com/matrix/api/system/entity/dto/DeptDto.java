package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 部门创建/修改 DTO */
@Data
@Schema(description = "部门DTO")
public class DeptDto {
    private Long id;
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private String leader;
    private Long leaderUserId;
    private String ancestors;
    private String deptCategory;
    private String phone;
    private String email;
    private Integer sort;
    private Integer status;
}
