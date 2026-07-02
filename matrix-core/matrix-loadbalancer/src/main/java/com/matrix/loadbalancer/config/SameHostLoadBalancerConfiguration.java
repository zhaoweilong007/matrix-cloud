package com.matrix.loadbalancer.config;

import com.matrix.loadbalancer.core.SameHostLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 同主机优先负载均衡自动配置。
 *
 * <p>通过 {@code matrix.loadbalancer.same-host.enabled=true} 启用（默认启用）。</p>
 *
 */
@AutoConfiguration
@ConditionalOnClass(ReactorLoadBalancer.class)
@ConditionalOnProperty(prefix = "matrix.loadbalancer.same-host", name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class SameHostLoadBalancerConfiguration {

    /**
     * 创建同主机优先负载均衡器 Bean
     *
     * @param environment              Spring 环境
     * @param loadBalancerClientFactory 负载均衡客户端工厂
     * @return SameHostLoadBalancer 实例
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> sameHostLoadBalancer(
            Environment environment, LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new SameHostLoadBalancer(name,
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class));
    }
}
