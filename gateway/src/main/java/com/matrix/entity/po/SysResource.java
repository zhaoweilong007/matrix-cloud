package com.matrix.entity.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysResource)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysResource extends BasePo<SysResource> {
    @TableId
    private Long id;

    private String name;

    private String url;

    private String description;

    private Long categoryId;




}

