package com.matrix.feign.loadbalancer;

import com.matrix.auto.properties.GaryLoadBalanceProperties;
import com.matrix.feign.chooser.IRuleChooser;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/28
 **/
public class VersionLoadBalancerClientFactory extends LoadBalancerClientFactory {


    private final IRuleChooser ruleChooser;
    private final GaryLoadBalanceProperties loadBalanceProperties;

    public VersionLoadBalancerClientFactory(LoadBalancerClientsProperties properties, IRuleChooser ruleChooser, GaryLoadBalanceProperties loadBalanceProperties) {
        super(properties);
        this.ruleChooser = ruleChooser;
        this.loadBalanceProperties = loadBalanceProperties;
    }

    @Override
    public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
        // 参考 {@link com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancerClientConfiguration#nacosLoadBalancer(Environment, LoadBalancerClientFactory, NacosDiscoveryProperties)} 方法
        return new VersionLoadBalancer(super.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId, ruleChooser, loadBalanceProperties);
    }

}
