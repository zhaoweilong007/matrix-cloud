package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 角色创建/修改 DTO。
 */
@Data
@Schema(description = "角色DTO")
public class SysRoleDto {

    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    private String code;

    /** 角色类型：1-内置 2-自定义 */
    private Integer type;

    /** 数据范围：1-全部 2-自定义 3-本部门 4-本部门及以下 5-仅本人 */
    private Integer dataScope;

    private String description;

    private Integer sort;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String remark;
}
