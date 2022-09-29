package com.matrix.config;

import cn.dev33.satoken.id.SaIdUtil;
import com.matrix.exception.GlobalExceptionHandler;
import com.matrix.filter.GlobalI18nFilter;
import com.matrix.filter.GlobalLogFilter;
import com.matrix.filter.GrayReactiveLoadBalancerClientFilter;
import com.matrix.filter.TenantReactorFilter;
import com.matrix.properties.SecurityProperties;
import com.matrix.properties.TenantProperties;
import com.matrix.security.SecurityAuth;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.Collectors;

/**
 * 描述：gateway配置
 *
 * @author zwl
 * @since 2022/8/2 10:16
 **/
@Configuration(proxyBeanMethods = false)
public class GateWayConfiguration {

    @Bean
    public RouteDefinitionRepository routeDefinitionRepository(ReactiveRedisTemplate<String, RouteDefinition> reactiveRedisTemplate) {
        return new RedisRouteDefinitionRepository(reactiveRedisTemplate);
    }

    @Bean
    public GrayReactiveLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, LoadBalancerProperties properties) {
        return new GrayReactiveLoadBalancerClientFilter(clientFactory, properties);
    }

    @Bean
    public GlobalI18nFilter globalI18nFilter() {
        return new GlobalI18nFilter();
    }

    @Bean
    public GlobalLogFilter globalLogFilter() {
        return new GlobalLogFilter();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


    @Bean
    public SecurityAuth securityAuth(SecurityProperties securityProperties) {
        return new SecurityAuth(securityProperties);
    }

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
