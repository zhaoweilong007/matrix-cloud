package com.matrix.tenant.core.service;


import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.common.util.cache.CacheUtils;
import com.matrix.tenant.api.client.ITenantApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

/**
 * Tenant 框架 Service 实现类
 */
@RequiredArgsConstructor
@Slf4j
public class TenantFrameworkServiceImpl implements ITenantFrameworkService {

    private final ITenantApi tenantApi;

    /**
     * 针对 {@link #getTenantIds()} 的缓存
     */
    private final LoadingCache<Object, List<Long>> getTenantIdsCache = CacheUtils.buildAsyncReloadingCache(
            // 过期时间 1 分钟
            Duration.ofMinutes(1L),
            new CacheLoader<>() {

                @Override
                public List<Long> load(Object key) {
                    return tenantApi.getTenantIdList().getData();
                }

            });

    /**
     * 针对 {@link #validTenant(Long)} 的缓存
     */
    private final LoadingCache<Long, R<Boolean>> validTenantCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<Long, R<Boolean>>() {

                @Override
                public R<Boolean> load(Long id) {
                    return tenantApi.validTenant(id);
                }

            });

    @Override
    @SneakyThrows
    public List<Long> getTenantIds() {
        return getTenantIdsCache.get(Boolean.TRUE);
    }

    @Override
    @SneakyThrows
    public void validTenant(Long id) {
        final R<Boolean> r = validTenantCache.get(id);
        R.throwOnFail(r, SystemErrorTypeEnum.VALID_TENANT_FAIL);
    }

}
