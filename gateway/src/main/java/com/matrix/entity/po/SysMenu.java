package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * (SysMenu)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Data
public class SysMenu extends BasePo<SysMenu> {
    @TableId
    private Long id;

    /**
     * 父级菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
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
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否显示
     */
    private Integer hidden;

    /**
     * 子鸡菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}

