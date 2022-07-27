package com.matrix.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:32
 **/
public class TenantContextHold {

    private static final ThreadLocal<Long> TENANT_CONTEXT = new TransmittableThreadLocal<>();
    private static final ThreadLocal<Boolean> TENANT_IGNORE = new TransmittableThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TENANT_CONTEXT.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_CONTEXT.get();
    }


    public static void setIgnore(Boolean ignore) {
        TENANT_IGNORE.set(ignore);
    }

    /**
     * 当前是否忽略租户
     *
     * @return 是否忽略
     */
    public static boolean isIgnore() {
        return Boolean.TRUE.equals(TENANT_IGNORE.get());
    }

    public static void clear() {
        TENANT_CONTEXT.remove();
        TENANT_IGNORE.remove();
    }
}
