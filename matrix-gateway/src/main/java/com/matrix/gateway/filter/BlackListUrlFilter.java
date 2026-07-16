package com.matrix.gateway.filter;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.gateway.order.FilterOrder;
import com.matrix.gateway.utils.WebFluxUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;

/**
 * 黑名单过滤器
 */
public class BlackListUrlFilter extends AbstractGatewayFilterFactory<BlackListUrlFilter.Config> implements Ordered {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public BlackListUrlFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String url = exchange.getRequest().getURI().getPath();
            if (config.matchBlacklist(url, PATH_MATCHER)) {
                return WebFluxUtils.webFluxResponseWriter(
                        exchange.getResponse(), R.fail(SystemErrorTypeEnum.FORBIDDEN));
            }

            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return FilterOrder.BLACK_LIST_URL_FILTER;
    }

    public static class Config {
        @Getter
        private List<String> blacklistUrl = new ArrayList<>();

        public boolean matchBlacklist(String url, AntPathMatcher pathMatcher) {
            return !blacklistUrl.isEmpty()
                    && blacklistUrl.stream().anyMatch(pattern -> pathMatcher.match(pattern.trim(), url));
        }

        public void setBlacklistUrl(List<String> blacklistUrl) {
            this.blacklistUrl = blacklistUrl;
        }
    }
}

