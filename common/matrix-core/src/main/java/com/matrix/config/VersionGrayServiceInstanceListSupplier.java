package com.matrix.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：版本服务提供
 *
 * @author zwl
 * @since 2022/9/29 16:00
 **/
@Slf4j
public class VersionGrayServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    public VersionGrayServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return getDelegate().get().map(serviceInstances -> this.filterByVersion(serviceInstances, request));
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return getDelegate().get();
    }

    private List<ServiceInstance> filterByVersion(List<ServiceInstance> instances, Request request) {
        Object context = request.getContext();
        if ((context instanceof RequestDataContext)) {
            HttpHeaders headers = ((RequestDataContext) context).getClientRequest().getHeaders();
            String version = headers.getFirst("version");
            if (StrUtil.isEmpty(version)) {
                return instances;
            }
            List<ServiceInstance> serviceInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getMetadata().get("version"), version))
                    .collect(Collectors.toList());

            if (CollectionUtil.isEmpty(serviceInstances)) {
                log.warn("No service instances found for  version : {}", version);
            }
            return serviceInstances;
        }
        return instances;
    }

}
