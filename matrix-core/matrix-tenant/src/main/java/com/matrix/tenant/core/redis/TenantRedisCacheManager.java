package com.matrix.tenant.core.redis;

import com.matrix.common.context.TenantContextHolder;
import com.matrix.redis.manager.PlusSpringCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * 多租户的 {@link RedisCacheManager} 实现类
 * <p>
 * 操作指定 name 的 {@link Cache} 时，自动拼接租户后缀，格式为 name + ":" + tenantId + 后缀
 */
@Slf4j
public class TenantRedisCacheManager extends PlusSpringCacheManager {


    @Override
    public Cache getCache(String name) {
        // 如果开启多租户，则 name 拼接租户后缀
        if (!TenantContextHolder.isIgnore()
                && TenantContextHolder.getTenantId() != null
                && !TenantContextHolder.getTenantId().equals(0L)) {
            name = name + ":" + TenantContextHolder.getTenantId();
        }

        // 继续基于父方法
        return super.getCache(name);
    }

}
