package com.matrix.config;

/**
 * 描述：负载均衡配置
 *
 * @author zwl
 * @since 2022/9/29 15:58
 **/

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.loadbalancer.support.SimpleObjectProvider;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 灰度负载配置
 *
 * @author zwl
 */
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = LoadBalanceAutoConfiguration.LoadBalanceConfig.class)
@AutoConfigureBefore({ReactorLoadBalancerClientAutoConfiguration.class,
        LoadBalancerBeanPostProcessorAutoConfiguration.class})
public class LoadBalanceAutoConfiguration {

    public static class LoadBalanceConfig {

        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
            ServiceInstanceListSupplier supplier = ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching().build(context);
            return new VersionGrayServiceInstanceListSupplier(supplier);
        }

        @Bean
        public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment, ServiceInstanceListSupplier serviceInstanceListSupplier) {
            String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
            return new RoundRobinLoadBalancer(new SimpleObjectProvider<>(serviceInstanceListSupplier), name);
        }

    }
}
