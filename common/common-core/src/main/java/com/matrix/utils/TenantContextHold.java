package com.matrix.utils;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:32
 **/
public class TenantContextHold {

    private static ThreadLocal<Long> tenantContext = new ThreadLocal<>();

    public static void setTenantId(Long tenant) {
        tenantContext.set(tenant);
    }

    public static Long getTenantId() {
        return tenantContext.get();
    }

    public static void clearTenant() {
        tenantContext.remove();
    }
}
