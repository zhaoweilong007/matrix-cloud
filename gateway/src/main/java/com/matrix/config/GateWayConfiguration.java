package com.matrix.config;

import com.matrix.filter.TenantReactorFilter;
import com.matrix.properties.SecurityProperties;
import com.matrix.properties.TenantProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 10:16
 **/
@Configuration(proxyBeanMethods = false)
public class GateWayConfiguration {

    @Bean
    public TenantReactorFilter tenantReactorFilter(TenantProperties tenantProperties) {
        return new TenantReactorFilter(tenantProperties);
    }
}
