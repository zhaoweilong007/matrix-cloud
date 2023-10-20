package com.matrix.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author zhaoweilong
 * @since 2023-07-01
 */
@Schema(description = "字典数据")
@Data
public class SysDictDataVo implements Serializable {

    private Long id;

    /**
     * 字典排序
     */
    @Schema(description = "字典排序")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Schema(description = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Schema(description = "字典键值")
    private String dictValue;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    /**
     * 表格回显样式
     */
    @Schema(description = "表格回显样式")
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @Schema(description = "是否默认")
    private Boolean isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "状态")
    private Boolean status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
