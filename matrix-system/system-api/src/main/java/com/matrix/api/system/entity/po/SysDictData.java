package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据实体。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Schema(description = "字典数据")
public class SysDictData extends TenantEntity {

    /** 字典类型编码（关联 sys_dict_type.type） */
    private String dictType;

    /** 字典标签（展示用） */
    private String label;

    /** 字典值 */
    private String value;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 颜色类型（CSS 样式，如 primary/success/danger） */
    private String colorType;

    /** CSS 类名 */
    private String cssClass;

    /** 备注 */
    private String remark;
}
