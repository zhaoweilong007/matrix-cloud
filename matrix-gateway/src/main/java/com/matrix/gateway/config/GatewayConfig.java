package com.matrix.gateway.config;

import com.matrix.auto.properties.GaryLoadBalanceProperties;
import com.matrix.auto.properties.GatewayProperties;
import com.matrix.auto.properties.RateLimiterProperties;
import com.matrix.auto.properties.XssProperties;
import com.matrix.common.constant.ConfigConstants;
import com.matrix.feign.chooser.IRuleChooser;
import com.matrix.gateway.filter.*;
import com.matrix.gateway.handler.GatewayExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * 网关配置
 *
 * @author ZhaoWeiLong
 * @since 2023/6/25
 **/
@Configuration
@EnableConfigurationProperties({GatewayProperties.class, XssProperties.class, RateLimiterProperties.class})
@Slf4j
public class GatewayConfig {

    @Bean
    public GlobalCorsFilter globalCorsFilter(GatewayProperties gatewayProperties) {
        return new GlobalCorsFilter(gatewayProperties);
    }

    @Bean
    public VersionPathRouteFilter versionPathRouteFilter() {
        return new VersionPathRouteFilter();
    }

    @Bean
    public GlobalCacheRequestFilter globalCacheRequestFilter() {
        return new GlobalCacheRequestFilter();
    }

    @Bean
    public GlobalI18nFilter globalI18nFilter() {
        return new GlobalI18nFilter();
    }

    @Bean
    public GlobalLogFilter globalLogFilter(GatewayProperties gatewayProperties) {
        return new GlobalLogFilter(gatewayProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "security.xss.enabled", havingValue = "true")
    public XssFilter xssFilter(XssProperties xssProperties) {
        return new XssFilter(xssProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "matrix.rate-limiter", name = "enabled", havingValue = "true")
    public RateLimiterGatewayFilter rateLimiterGatewayFilter(
            RedissonClient redissonClient, RateLimiterProperties rateLimiterProperties) {
        return new RateLimiterGatewayFilter(redissonClient, rateLimiterProperties);
    }

    @Bean
    @Order(-1)
    public GatewayExceptionHandler gatewayExceptionHandler() {
        return new GatewayExceptionHandler();
    }

    /**
     * 偏向性路由
     */
    @Bean
    public DeflectionIntanceFilter deflectionRouteFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new DeflectionIntanceFilter(clientFactory, properties);
    }

    /**
     * 一致性hash路由
     */
    @Bean
    public IpHashLoadBalancerClientFilter ipHashLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new IpHashLoadBalancerClientFilter(clientFactory, properties);
    }

    /**
     * 灰度路由
     */
    @Bean
    @ConditionalOnProperty(
            prefix = ConfigConstants.CONFIG_LOADBALANCE_ISOLATION,
            name = "enabled",
            havingValue = "true")
    public GrayVersionIsolationFilter grayReactiveLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory,
            GatewayLoadBalancerProperties properties,
            IRuleChooser ruleChooser,
            GaryLoadBalanceProperties loadBalanceProperties) {
        return new GrayVersionIsolationFilter(clientFactory, properties, ruleChooser, loadBalanceProperties);
    }

    @Bean(value = "remoteAddrKeyResolver")
    @Primary
    public KeyResolver remoteAddrKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
