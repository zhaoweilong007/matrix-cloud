package com.matrix.gateway.filter;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.gateway.order.FilterOrder;
import com.matrix.gateway.utils.WebFluxUtils;
import lombok.Getter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 黑名单过滤器
 */
public class BlackListUrlFilter extends AbstractGatewayFilterFactory<BlackListUrlFilter.Config> implements Ordered {
    public BlackListUrlFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String url = exchange.getRequest().getURI().getPath();
            if (config.matchBlacklist(url)) {
                return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), R.fail(SystemErrorTypeEnum.FORBIDDEN));
            }

            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return FilterOrder.BLACK_LIST_URL_FILTER;
    }

    public static class Config {
        private final List<Pattern> blacklistUrlPattern = new ArrayList<>();
        @Getter
        private List<String> blacklistUrl;

        public boolean matchBlacklist(String url) {
            return !blacklistUrlPattern.isEmpty() && blacklistUrlPattern.stream().anyMatch(p -> p.matcher(url).find());
        }

        public void setBlacklistUrl(List<String> blacklistUrl) {
            this.blacklistUrl = blacklistUrl;
            this.blacklistUrlPattern.clear();
            this.blacklistUrl.forEach(url -> {
                this.blacklistUrlPattern.add(Pattern.compile(url.replaceAll("\\*\\*", "(.*?)"), Pattern.CASE_INSENSITIVE));
            });
        }
    }

}
