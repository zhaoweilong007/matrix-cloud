package com.matrix.entity.po;

import lombok.Data;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:15
 **/
@Data
public class TenantPo<T extends TenantPo<?>> extends BasePo<T> {
    private Long tenantId;
}
