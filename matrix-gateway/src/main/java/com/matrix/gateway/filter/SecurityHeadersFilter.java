package com.matrix.gateway.filter;

import com.matrix.gateway.order.FilterOrder;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 安全响应头过滤器。
 *
 * <p>为所有 HTTP 响应添加安全相关的响应头，包括 HSTS、X-Frame-Options、
 * X-Content-Type-Options、Referrer-Policy 和 Content-Security-Policy。</p>
 */
public class SecurityHeadersFilter implements WebFilter, Ordered {

    /** HSTS：强制浏览器使用 HTTPS，max-age=1年 */
    private static final String HSTS_HEADER = "max-age=31536000; includeSubDomains";

    /** 默认 CSP：允许同源脚本/样式/图片/字体/媒体/连接 */
    private static final String CSP_HEADER =
            "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.add("Strict-Transport-Security", HSTS_HEADER);
        headers.add("X-Frame-Options", "DENY");
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("Referrer-Policy", "strict-origin-when-cross-origin");
        headers.add("Content-Security-Policy", CSP_HEADER);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.SECURITY_HEADERS_FILTER;
    }
}
