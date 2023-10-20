package com.matrix.feign.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.matrix.auto.properties.GaryLoadBalanceProperties;
import com.matrix.common.constant.CommonConstants;
import com.matrix.feign.chooser.IRuleChooser;
import com.matrix.feign.utils.QueryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 自定义版本路由选择
 */
@Slf4j
@RequiredArgsConstructor
public class VersionLoadBalancer implements ReactorServiceInstanceLoadBalancer {


    /**
     * 服务实例的列表
     */
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;

    /**
     * 服务实例名
     */
    private final String serviceId;

    /**
     * 路由策略
     */
    private final IRuleChooser ruleChooser;


    private final GaryLoadBalanceProperties loadBalanceProperties;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        // 从request中获取版本，兼容webflux方式
        RequestData requestData = ((RequestDataContext) (request.getContext())).getClientRequest();
        final ServiceInstanceListSupplier supplier = serviceInstanceListSuppliers.getIfAvailable(NoopServiceInstanceListSupplier::new);
        String version = getVersionFromRequestData(requestData);
        return supplier.get(request).next().map(instanceList -> getInstanceResponse(instanceList, version));
    }

    private String getVersionFromRequestData(RequestData requestData) {
        Map<String, String> queryMap = QueryUtils.getQueryMap(requestData.getUrl());
        if (requestData.getHeaders().containsKey(CommonConstants.VERSION_HEADER)) {
            return requestData.getHeaders().getFirst(CommonConstants.VERSION_HEADER);
        } else if (MapUtils.isNotEmpty(queryMap) && queryMap.containsKey(CommonConstants.VERSION_HEADER)
                && StringUtils.isNotBlank(queryMap.get(CommonConstants.VERSION_HEADER))) {
            return queryMap.get(CommonConstants.VERSION_HEADER);
        }
        return null;
    }

    /**
     * 1. 先获取到拦截的版本，如果不为空的话就将service列表过滤，寻找metadata中哪个服务是配置的版本，
     * 如果版本为空则不需要进行过滤直接提交给service选择器进行选择
     * 2. 如果没有找到版本对应的实例，则找所有版本为空或者版本号为default的实例
     * 3.将instance列表提交到选择器根据对应的策略返回一个instance
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String version) {
        // 如果服务实例为空，则直接返回
        if (CollUtil.isEmpty(instances)) {
            log.warn("[getInstanceResponse][serviceId({}) 服务实例列表为空]", serviceId);
            return new EmptyResponse();
        }
        //对服务列表进行版本号过滤
        List<ServiceInstance> filteredServiceIstanceList = null;
        if (StringUtils.isNotBlank(version)) {
            if (CollectionUtils.isNotEmpty(instances)) {
                filteredServiceIstanceList = instances.stream()
                        .filter(item -> item.getMetadata().containsKey(CommonConstants.VERSION_HEADER) &&
                                version.equals(item.getMetadata().get(CommonConstants.VERSION_HEADER)))
                        .collect(Collectors.toList());
            }
        }
        // 如果没有找到对应的版本实例时，选择版本号为空的或这版本为default的实例
        if (CollectionUtils.isEmpty(filteredServiceIstanceList)) {
            filteredServiceIstanceList = instances.stream()
                    .filter(item -> !item.getMetadata().containsKey(CommonConstants.VERSION_HEADER) ||
                            StringUtils.isBlank(item.getMetadata().get(CommonConstants.VERSION_HEADER))
                            || Objects.equals(StrUtil.toString(item.getMetadata().get(CommonConstants.VERSION_HEADER)), loadBalanceProperties.getDefaultVersion()))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(filteredServiceIstanceList)) {
            log.warn("[getInstanceResponse][serviceId({}) 没有满足版本({})的服务实例列表]", serviceId, version);
            return new EmptyResponse();
        }
        // 经过两轮过滤后如果能找到的话就选择，不然返回空
        ServiceInstance serviceInstance = this.ruleChooser.choose(filteredServiceIstanceList);
        if (!Objects.isNull(serviceInstance)) {
            log.debug("[getInstanceResponse] chose serviceId:[{}],version:{},address:[{}:{}]", serviceId, version
                    , serviceInstance.getHost(), serviceInstance.getPort());
            return new DefaultResponse(serviceInstance);
        }
        log.warn("[getInstanceResponse][serviceId({}) 没有满足选择器的服务实例列表]", serviceId);
        // 返回空的返回体
        return new EmptyResponse();
    }
}
