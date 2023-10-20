package com.matrix.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {

    /**
     * 字典排序
     */
    @Schema(description = "字典排序")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Schema(description = "字典标签", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Schema(description = "字典键值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @Schema(description = "样式属性（其他样式扩展）", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /**
     * 表格字典样式
     */
    @Schema(description = "表格字典样式")
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
