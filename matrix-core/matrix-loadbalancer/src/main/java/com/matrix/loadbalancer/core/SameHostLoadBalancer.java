package com.matrix.loadbalancer.core;

import cn.hutool.core.net.NetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 同主机优先负载均衡器。
 *
 * <p>优先选择与调用方在同一台机器上的服务实例，减少网络跳数。
 * 若无同主机实例，则随机选择一个可用实例。</p>
 *
 */
@Slf4j
@AllArgsConstructor
public class SameHostLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * 服务 ID
     */
    private final String serviceId;
    /**
     * 服务实例列表供应商
     */
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
    }

    /**
     * 处理服务实例响应，在返回前回调 SelectedInstanceCallback
     *
     * @param supplier          服务实例列表供应商
     * @param serviceInstances 服务实例列表
     * @return 选中的服务实例响应
     */
    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    /**
     * 从实例列表中选取一个服务实例，优先选择同主机实例，否则随机选择
     *
     * @param instances 服务实例列表
     * @return 选中的服务实例响应
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: {}", serviceId);
            }
            return new EmptyResponse();
        }
        // 优先匹配本机 IP
        for (ServiceInstance instance : instances) {
            if (NetUtil.localIpv4s().contains(instance.getHost())) {
                return new DefaultResponse(instance);
            }
        }
        // 无同主机实例，随机选择
        return new DefaultResponse(instances.get(ThreadLocalRandom.current().nextInt(instances.size())));
    }
}
