package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;

/**
 * 多租户上下文 Holder
 */
public class TenantContextHolder {

    /**
     * 当前租户编号
     */
    private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 是否忽略租户
     */
    private static final ThreadLocal<Boolean> IGNORE = new TransmittableThreadLocal<>();

    /**
     * 获得租户编号。
     *
     * @return 租户编号
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 获得租户编号。如果不存在，则抛出 NullPointerException 异常
     *
     * @return 租户编号
     */
    public static Long getRequiredTenantId() {
        Long tenantId = getTenantId();
        if (tenantId == null) {
            throw new ServiceException(SystemErrorTypeEnum.TENANT_NOT_FOUND);
        }
        return tenantId;
    }

    /**
     * 当前是否忽略租户
     *
     * @return 是否忽略
     */
    public static boolean isIgnore() {
        return Boolean.TRUE.equals(IGNORE.get());
    }

    public static void setIgnore(Boolean ignore) {
        IGNORE.set(ignore);
    }

    public static void clear() {
        TENANT_ID.remove();
        IGNORE.remove();
    }

}
