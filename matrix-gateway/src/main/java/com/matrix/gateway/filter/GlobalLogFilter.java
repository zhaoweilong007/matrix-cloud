package com.matrix.gateway.filter;

import cn.hutool.core.map.MapUtil;
import com.matrix.auto.properties.GatewayProperties;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.gateway.order.FilterOrder;
import com.matrix.gateway.utils.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * 全局日志过滤器。
 *
 * <p>请求参数打印 + 响应体捕获 + 敏感字段脱敏 + 请求耗时统计。</p>
 *
 * @author matrix
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";
    private static final String RESPONSE_BODY = "responseBody";

    /** 日志中需要移除的敏感字段 */
    private static final Set<String> EXCLUDE_PROPERTIES = Set.of(
            "password", "oldPassword", "newPassword", "confirmPassword",
            "oldPwd", "newPwd", "pwd", "secret", "token", "accessToken",
            "refreshToken", "credential");

    private final GatewayProperties gatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!gatewayProperties.getRequestLog()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = WebFluxUtils.getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + path;

        // 打印请求参数
        logRequest(exchange, url);

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());

        // P1: 包装 Response，捕获响应体
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, url);

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(START_TIME);
                    String responseBody = exchange.getAttribute(RESPONSE_BODY);
                    if (startTime != null) {
                        long executeTime = System.currentTimeMillis() - startTime;
                        String truncatedBody = responseBody != null && responseBody.length() > 500
                                ? responseBody.substring(0, 500) + "..." : responseBody;
                        log.info("结束请求 => URL[{}],响应:[{}],耗时:[{}]毫秒", url, truncatedBody, executeTime);
                    }
                }));
    }

    private void logRequest(ServerWebExchange exchange, String url) {
        if (WebFluxUtils.isJsonRequest(exchange)) {
            String jsonParam = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
            if (jsonParam != null && !jsonParam.isEmpty()) {
                jsonParam = removeSensitiveFields(jsonParam);
            }
            log.info("开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
        } else {
            MultiValueMap<String, String> parameterMap = exchange.getRequest().getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String params = JsonUtils.toJsonString(parameterMap);
                log.info("开始请求 => URL[{}],参数类型[param],参数:[{}]", url, params);
            } else {
                log.info("开始请求 => URL[{}],无参数", url);
            }
        }
    }

    /** P1: 包装 Response，捕获响应体内容 */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, String url) {
        ServerHttpResponse response = exchange.getResponse();
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux<? extends DataBuffer> fluxBody) {
                    String contentType = exchange.getResponse().getHeaders()
                            .getFirst("Content-Type");
                    if (contentType != null && contentType.contains("application/json")) {
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            DataBufferFactory bufferFactory = response.bufferFactory();
                            DataBuffer join = new DefaultDataBufferFactory().join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            // 释放内存
                            org.springframework.core.io.buffer.DataBufferUtils.release(join);

                            String responseResult = new String(content, StandardCharsets.UTF_8);
                            exchange.getAttributes().put(RESPONSE_BODY, responseResult);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                return super.writeWith(body);
            }
        };
    }

    /** P1: 移除 JSON 参数中的敏感字段（正则替换为 ***） */
    private String removeSensitiveFields(String jsonParam) {
        String result = jsonParam;
        for (String field : EXCLUDE_PROPERTIES) {
            // 匹配 "field": "value" 或 "field": null 或 "field": 123 模式
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*\"[^\"]*\"",
                    "\"" + field + "\":\"***\"");
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*[^,}\\]]+",
                    "\"" + field + "\":\"***\"");
        }
        return result;
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_LOG_FILTER;
    }
}
