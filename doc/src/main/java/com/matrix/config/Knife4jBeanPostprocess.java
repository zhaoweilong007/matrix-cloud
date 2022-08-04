package com.matrix.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jAggregationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/4 10:46
 **/
@Component
public class Knife4jBeanPostprocess implements BeanPostProcessor {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Knife4jAggregationProperties) {
            Knife4jAggregationProperties aggregationProperties = (Knife4jAggregationProperties) bean;
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            try {
                List<String> services = discoveryClient.getServices();
                Map<String, Instance> map = services.stream().filter(s -> !s.equals(appName)).collect(Collectors.toMap(s -> s, s -> {
                    try {
                        return namingService.selectOneHealthyInstance(s, nacosDiscoveryProperties.getGroup());
                    } catch (NacosException e) {
                        e.printStackTrace();
                        return null;
                    }
                }, (o, o2) -> o2));

                List<NacosRoute> routeList = map.entrySet().stream().map((entry) -> {
                    NacosRoute nacosRoute = new NacosRoute();
                    nacosRoute.setName(entry.getValue().getServiceName());
                    nacosRoute.setServiceName(entry.getKey());
                    nacosRoute.setServicePath(entry.getKey());
                    nacosRoute.setLocation("/v2/api-docs");
                    return nacosRoute;
                }).collect(Collectors.toList());

                aggregationProperties.getNacos().setRoutes(routeList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }
}
