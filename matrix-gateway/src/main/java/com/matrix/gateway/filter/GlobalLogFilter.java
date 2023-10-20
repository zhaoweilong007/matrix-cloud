package com.matrix.gateway.filter;

import cn.hutool.core.map.MapUtil;
import com.matrix.auto.properties.GatewayProperties;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.gateway.order.FilterOrder;
import com.matrix.gateway.utils.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局日志过滤器
 * <p>
 * 用于打印请求执行参数与响应时间等等
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";
    private final GatewayProperties gatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!gatewayProperties.getRequestLog()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = WebFluxUtils.getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + path;

        String parameters = "";
        // 打印请求参数
        if (WebFluxUtils.isJsonRequest(exchange)) {
            parameters = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
            log.debug("开始请求 => URL[{}],参数类型[json],参数:[{}]", url, parameters);
        } else {
            MultiValueMap<String, String> parameterMap = request.getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                parameters = JsonUtils.toJsonString(parameterMap);
                log.debug("开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                log.debug("开始请求 => URL[{}],无参数", url);
            }
        }
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.debug("结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
            }
        }));
    }


    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_LOG_FILTER;
    }

}
