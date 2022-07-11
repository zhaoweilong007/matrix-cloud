package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * (SysRole)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysRole extends BasePo<SysRole> {
    @TableId
    private Long id;

    private String name;

    private String description;

    private Integer adminCount;


    private Integer status;

    private Integer sort;




}

