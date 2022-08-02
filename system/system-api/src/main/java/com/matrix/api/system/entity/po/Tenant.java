package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.entity.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/27 15:17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_tenant")
public class Tenant extends BasePo<Tenant> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户状态：启用、禁用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 租户名称
     */
    @TableField(value = "tenant_name")
    private String tenantName;

    public static final String COL_ID = "id";

    public static final String COL_STATUS = "status";

    public static final String COL_TENANT_NAME = "tenant_name";
}