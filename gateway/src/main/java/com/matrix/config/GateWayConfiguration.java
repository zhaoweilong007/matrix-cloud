package com.matrix.config;

import cn.dev33.satoken.id.SaIdUtil;
import com.matrix.filter.TenantReactorFilter;
import com.matrix.properties.TenantProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.Collectors;

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

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 刷新ID-token，每五分钟一次
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshToken() {
        SaIdUtil.refreshToken();
    }
}
