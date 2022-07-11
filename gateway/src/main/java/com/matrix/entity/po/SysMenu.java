package com.matrix.entity.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

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

    private Long parentId;

    private String title;

    private Integer level;

    private Integer sort;

    private String name;

    private String icon;

    private Integer hidden;



}

