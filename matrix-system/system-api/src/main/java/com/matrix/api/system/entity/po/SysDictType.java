package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型实体。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
@Schema(description = "字典类型")
public class SysDictType extends TenantEntity {

    /** 字典名称 */
    private String name;

    /** 字典类型编码（唯一） */
    private String type;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 备注 */
    private String remark;
}
