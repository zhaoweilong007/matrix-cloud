package com.matrix.entity.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/13 15:26
 **/
@Data
public class SysRoleDto {

    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    private String description;

    @Digits(integer = 1, fraction = 0, message = "必须是正整数")
    @NotNull(message = "是否启用不能为空")
    private Integer status;

}
