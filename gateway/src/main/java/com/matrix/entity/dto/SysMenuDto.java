package com.matrix.entity.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/13 16:21
 **/
@Data
public class SysMenuDto {

    private Long id;

    /**
     * 父级菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    /**
     * 菜单级数
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 前端名称
     */
    @NotBlank(message = "前端名称不能为空")
    private String name;

    /**
     * 菜单图标
     */
    @NotBlank(message = "菜单图标不能为空")
    private String icon;

    /**
     * 是否显示 1显示 0隐藏
     */
    @Digits(integer = 1, fraction = 0, message = "必须是正整数")
    @NotNull(message = "是否显示不能为空")
    private Integer hidden;

}
