package com.matrix.tenant.core.mq;

import com.matrix.common.context.TenantContextHolder;
import com.matrix.mq.redis.core.RedisMessageInterceptor;
import com.matrix.mq.redis.message.AbstractRedisMessage;

/**
 * Redis MQ 租户上下文传播拦截器。
 *
 * <p>发送消息时自动将 tenantId 注入消息头，
 * 消费消息时自动从消息头恢复 tenantId 到上下文。</p>
 *
 */
public class TenantRedisMessageInterceptor implements RedisMessageInterceptor {

    private static final String HEADER_TENANT_ID = "tenant-id";

    @Override
    public void sendMessageBefore(AbstractRedisMessage message) {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            message.addHeader(HEADER_TENANT_ID, tenantId.toString());
        }
    }

    @Override
    public void consumeMessageBefore(AbstractRedisMessage message) {
        String tenantIdStr = message.getHeader(HEADER_TENANT_ID);
        if (tenantIdStr != null) {
            TenantContextHolder.setTenantId(Long.parseLong(tenantIdStr));
        }
    }

    @Override
    public void consumeMessageAfter(AbstractRedisMessage message) {
        TenantContextHolder.clear();
    }
}
