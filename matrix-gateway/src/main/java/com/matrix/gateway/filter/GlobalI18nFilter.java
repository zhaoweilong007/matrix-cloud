package com.matrix.gateway.filter;

import com.matrix.gateway.order.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * 全局国际化处理
 */
@Slf4j
public class GlobalI18nFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String language = exchange.getRequest().getHeaders().getFirst("content-language");
        Locale locale = Locale.getDefault();
        if (language != null && !language.isEmpty()) {
            String[] split = language.split("_");
            locale = new Locale(split[0], split[1]);
        }
        LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(locale), true);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_I18N_FILTER;
    }

}
