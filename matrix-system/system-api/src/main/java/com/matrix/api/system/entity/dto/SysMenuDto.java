package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单创建/修改 DTO。
 */
@Data
@Schema(description = "菜单DTO")
public class SysMenuDto {

    private Long id;

    /** 父级菜单 ID，0 表示根 */
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String title;

    /** 菜单级数（自动计算） */
    private Integer level;

    /** 排序 */
    private Integer sort;

    @NotBlank(message = "前端名称不能为空")
    private String name;

    /** 菜单图标 */
    private String icon;

    /** 是否隐藏：1-是 0-否 */
    private Integer hidden;

    /** 菜单类型：1-目录 2-菜单 3-按钮 */
    private Integer type;

    /** 权限标识 */
    private String permission;

    /** 前端路由路径 */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** keep-alive 组件名 */
    private String componentName;

    /** 是否可见：1-显示 0-隐藏 */
    private Integer visible;

    /** 是否缓存：1-是 0-否 */
    private Integer keepAlive;

    /** 是否始终显示：1-是 0-否 */
    private Integer alwaysShow;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
