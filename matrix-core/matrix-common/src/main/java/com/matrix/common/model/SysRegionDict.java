package com.matrix.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseIdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 行政区域表
 * </p>
 *
 * @author zhaoweilong
 * @since 2023-07-01
 */
@Data
@TableName("sys_region_dict")
public class SysRegionDict extends BaseIdEntity implements Serializable {


    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 拼音简写
     */
    private String pinYin;

    /**
     * 上级编码
     */
    private String parentCode;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 区域类型(1 省 2 市 3 区/县 4 小区/镇 5 村)
     */
    private Integer areaType;
}
