package com.matrix.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 描述：gateway基于nacos路由动态配置
 *
 * @author zwl
 * @since 2022/7/4 14:26
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class NacosDynamicRouteConfig {

    private final RouteDefinitionWriter routeDefinitionWriter;

    private final ApplicationEventPublisher publisher;

    private final ConfigService configService;

    private final NacosConfigProperties nacosConfigProperties;

    public static String ROUTE_DATA_ID = "gateway-route";


    @PostConstruct
    public void init() throws Exception {
        String config = configService.getConfig(ROUTE_DATA_ID, nacosConfigProperties.getGroup(), 3000);
        processConfigInfo(config, routeDefinition -> routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe());
        configService.addListener(ROUTE_DATA_ID, nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("NacosDynamicRouteConfig receiveConfigInfo。。。");
                processConfigInfo(configInfo, routeDefinition -> {
                    try {
                        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
                    } catch (Exception e) {
                        log.warn("NacosDynamicRouteConfig delete routeDefinition error", e);
                    }
                    routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
                });
                //发布事件，让路由重新加载
                publisher.publishEvent(new RefreshRoutesEvent(this));
            }
        });
    }

    private void processConfigInfo(String configInfo, Consumer<RouteDefinition> consumer) {
        List<RouteDefinition> routes = JSON.parseArray(configInfo, RouteDefinition.class);
        if (CollectionUtil.isEmpty(routes)) {
            return;
        }
        routes.forEach(consumer);
    }


}
