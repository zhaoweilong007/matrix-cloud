package com.matrix.utils;

import com.matrix.properties.TenantProperties;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/27 11:15
 **/
public class ReactiveTenantCTH {

    public static final String TENANT_IGNORE = "TENANT_IGNORE";

    public static Mono<Long> getTenantId() {
        return Mono.deferContextual(contextView -> {
            if (contextView.isEmpty()) {
                return Mono.empty();
            }
            Long tenantId = contextView.get(TenantProperties.TENANT_KEY);
            return Mono.just(tenantId);
        });
    }

    public static Mono<Boolean> isIgnore() {
        return Mono.deferContextual(contextView -> {
            if (contextView.isEmpty()) {
                return Mono.just(false);
            }
            Boolean flag = contextView.get(TENANT_IGNORE);
            return Mono.just(flag);
        });
    }


    public static void put(Context context, Long tenantId) {
        context.put(TenantProperties.TENANT_KEY, tenantId);
    }


    public static void setignore(Context context) {
        context.put(TENANT_IGNORE, Boolean.TRUE);
    }


}
