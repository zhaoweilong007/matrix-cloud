package com.matrix.gateway.filter;

import com.matrix.common.constant.CommonConstants;
import com.matrix.gateway.order.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL 路径版本路由过滤器。
 *
 * <p>从请求路径中提取版本前缀（如 {@code /v1/system/user}），
 * 将版本号注入 {@code version} 请求头，并重写 URI 去掉版本段。
 * 后续由 {@link GrayVersionIsolationFilter} 根据版本头筛选服务实例。</p>
 *
 * <p>示例：
 * <pre>{@code
 * GET /v1/resource/user/list
 *   → 提取 version=v1
 *   → 重写 URI 为 /resource/user/list
 *   → 设置 header: version=v1
 * }</pre></p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Slf4j
public class VersionPathRouteFilter implements GlobalFilter, Ordered {

    /** 匹配 URL 路径中的版本前缀 /v{digits}/ */
    private static final Pattern VERSION_PATH_PATTERN = Pattern.compile("^/v(\\d+)(/.*)?");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getRawPath();

        Matcher matcher = VERSION_PATH_PATTERN.matcher(path);
        if (!matcher.matches()) {
            return chain.filter(exchange);
        }

        String version = "v" + matcher.group(1);
        String remainingPath = matcher.group(2);
        if (remainingPath == null || remainingPath.isEmpty()) {
            remainingPath = "/";
        }

        // 注入版本号到请求头，供 GrayLoadBalancer 使用
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CommonConstants.VERSION_HEADER, version)
                .uri(rewriteUri(request.getURI(), remainingPath))
                .build();

        log.debug("VersionPathRoute: version={}, path={} → {}", version, path, remainingPath);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private URI rewriteUri(URI originalUri, String newPath) {
        return UriComponentsBuilder.fromUri(originalUri)
                .replacePath(newPath)
                .replaceQuery(originalUri.getRawQuery())
                .build(true)
                .toUri();
    }

    @Override
    public int getOrder() {
        return FilterOrder.VERSION_PATH_ROUTE_FILTER;
    }
}
