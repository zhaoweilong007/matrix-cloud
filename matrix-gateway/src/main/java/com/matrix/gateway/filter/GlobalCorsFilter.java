package com.matrix.gateway.filter;

import com.matrix.auto.properties.GatewayProperties;
import com.matrix.gateway.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * 跨域配置
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalCorsFilter implements WebFilter, Ordered {

    private static final String ALLOWED_EXPOSE = "*";
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String MAX_AGE = "18000L";

    private final GatewayProperties gatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
            headers.add("Access-Control-Max-Age", MAX_AGE);
            headers.add("Access-Control-Allow-Credentials", "true");
            setCorsHeader(request.getHeaders().getOrigin(), headers);

            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }


    private void setCorsHeader(String reqOrigin, HttpHeaders headers) {
        if (reqOrigin == null) {
            return;
        }
        if (gatewayProperties.getAllowOrigins().contains(reqOrigin)) {
            headers.add("Access-Control-Allow-Origin", reqOrigin);
        } else {
            log.warn("cors origin not allow:{}", reqOrigin);
        }
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_CORS_FILTER;
    }
}
