package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐实体。
 *
 * <p>定义租户可使用的菜单和功能范围。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_package")
@Schema(description = "租户套餐")
public class SysTenantPackage extends TenantEntity {

    /** 套餐名称 */
    private String name;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 关联菜单 ID 集合（JSON 数组字符串） */
    private String menuIds;

    /** 备注 */
    private String remark;
}
