package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 菜单实体。
 *
 * <p>支持目录、菜单、按钮三种类型，包含前端路由元数据。</p>
 */
@Data
@Schema(description = "菜单")
public class SysMenu extends TenantEntity {

    @TableId
    private Long id;

    /** 父级菜单 ID，0 表示根 */
    private Long parentId;

    /** 菜单名称 */
    private String title;

    /** 菜单级数 */
    private Integer level;

    /** 排序 */
    private Integer sort;

    /** 前端路由名称 */
    private String name;

    /** 菜单图标 */
    private String icon;

    /** 是否隐藏（0-不隐藏 1-隐藏） */
    private Integer hidden;

    /** 是否可见（1-显示 0-隐藏），与 hidden 互补 */
    private Integer visible;

    /** 菜单类型：1-目录 2-菜单 3-按钮 */
    private Integer type;

    /** 权限标识（如 system:user:list） */
    private String permission;

    /** 前端路由路径（如 /system/user） */
    private String path;

    /** 前端组件路径（如 system/user/index） */
    private String component;

    /** keep-alive 组件名 */
    private String componentName;

    /** 是否缓存：1-是 0-否 */
    private Integer keepAlive;

    /** 是否始终显示：1-是 0-否 */
    private Integer alwaysShow;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 子菜单列表（非数据库字段） */
    @TableField(exist = false)
    private List<SysMenu> children;
}
