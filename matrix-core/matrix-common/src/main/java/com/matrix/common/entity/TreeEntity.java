package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<T> extends BaseEntity {


    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();

}
