package com.matrix.mq.tenant;

import com.aliyun.openservices.ons.api.Message;
import com.matrix.common.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * MQ 租户上下文传播工具
 * <p>
 * 在消息发送前注入租户ID，在消息消费前恢复租户上下文
 * </p>
 *
 * @author matrix
 */
@Slf4j
public class TenantMqUtils {

    /**
     * 租户ID 消息属性 Key
     */
    public static final String TENANT_ID_KEY = "tenantId";

    /**
     * 忽略租户标志 消息属性 Key
     */
    public static final String IGNORE_TENANT_KEY = "ignoreTenant";

    /**
     * 在消息中注入租户上下文
     *
     * @param message 消息对象
     */
    public static void injectTenantContext(Message message) {
        if (message == null) {
            return;
        }

        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            message.putUserProperties(TENANT_ID_KEY, String.valueOf(tenantId));
            log.debug("注入租户上下文到消息: tenantId={}", tenantId);
        }

        if (TenantContextHolder.isIgnore()) {
            message.putUserProperties(IGNORE_TENANT_KEY, "true");
            log.debug("注入忽略租户标志到消息");
        }
    }

    /**
     * 从消息中恢复租户上下文
     *
     * @param message 消息对象
     */
    public static void restoreTenantContext(Message message) {
        if (message == null) {
            return;
        }

        try {
            String tenantIdStr = message.getUserProperties(TENANT_ID_KEY);
            if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
                Long tenantId = Long.parseLong(tenantIdStr);
                TenantContextHolder.setTenantId(tenantId);
                log.debug("从消息恢复租户上下文: tenantId={}", tenantId);
            }

            String ignoreTenant = message.getUserProperties(IGNORE_TENANT_KEY);
            if ("true".equals(ignoreTenant)) {
                TenantContextHolder.setIgnore(true);
                log.debug("从消息恢复忽略租户标志");
            }
        } catch (Exception e) {
            log.warn("从消息恢复租户上下文失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 清理租户上下文
     */
    public static void clearTenantContext() {
        TenantContextHolder.clear();
    }

    /**
     * 在 Runnable 中传播租户上下文
     *
     * @param runnable 原始 Runnable
     * @return 包装后的 Runnable
     */
    public static Runnable wrapRunnable(Runnable runnable) {
        Long tenantId = TenantContextHolder.getTenantId();
        boolean ignore = TenantContextHolder.isIgnore();
        return () -> {
            try {
                if (tenantId != null) {
                    TenantContextHolder.setTenantId(tenantId);
                }
                TenantContextHolder.setIgnore(ignore);
                runnable.run();
            } finally {
                TenantContextHolder.clear();
            }
        };
    }
}
