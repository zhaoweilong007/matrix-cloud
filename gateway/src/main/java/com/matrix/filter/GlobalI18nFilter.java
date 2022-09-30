package com.matrix.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import reactor.core.publisher.Mono;

/**
 * 全局国际化处理
 */
@Slf4j
public class GlobalI18nFilter implements GlobalFilter, Ordered {

    private final AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String language = exchange.getRequest().getHeaders().getFirst("Accept-Language");
        if (StrUtil.isEmpty(language)) {
            return chain.filter(exchange);
        }
        LocaleContext localeContext = resolver.resolveLocaleContext(exchange);
        LocaleContextHolder.setLocaleContext(localeContext);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
