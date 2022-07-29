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
        return Mono.subscriberContext().map(context -> context.get(TENANT_IGNORE));
    }


    public static Context put(Context context, Long tenantId) {
        return context.put(TenantProperties.TENANT_KEY, tenantId);
    }


    public static Context setignore(Context context) {
        return Context.of(TENANT_IGNORE, true);
    }


}
