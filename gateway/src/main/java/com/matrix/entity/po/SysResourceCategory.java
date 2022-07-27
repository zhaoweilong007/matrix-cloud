package com.matrix.entity.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysResourceCategory)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysResourceCategory extends TenantPo<SysResourceCategory> {
    @TableId
    private Long id;

    private String name;

    private Integer sort;



}

