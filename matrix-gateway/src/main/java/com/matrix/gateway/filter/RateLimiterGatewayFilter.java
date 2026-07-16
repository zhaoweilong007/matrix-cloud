package com.matrix.gateway.filter;

import com.matrix.auto.properties.RateLimiterProperties;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.gateway.order.FilterOrder;
import com.matrix.gateway.utils.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关级分布式限流过滤器。
 *
 * <p>基于 Redisson {@link RRateLimiter} 令牌桶算法，
 * 支持 IP 级别和用户级别限流。配置由 {@code matrix.rate-limiter.*} 控制。</p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Slf4j
@RequiredArgsConstructor
public class RateLimiterGatewayFilter implements GlobalFilter, Ordered {

    private static final String RATE_LIMITER_KEY_PREFIX = "matrix:rate_limiter:";

    private final RedissonClient redissonClient;
    private final RateLimiterProperties properties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        String key = resolveKey(exchange);
        String bucketKey = RATE_LIMITER_KEY_PREFIX + key;

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(bucketKey);
        // 使用 trySetRateAsync 异步调用，并在其完成后再 tryAcquireAsync，确保 WebFlux 事件循环不被阻塞
        return Mono.fromFuture(rateLimiter.trySetRateAsync(RateType.OVERALL, properties.getReplenishRate(),
                        properties.getBurstCapacity(), RateIntervalUnit.SECONDS).toCompletableFuture())
                .flatMap(unused -> Mono.fromFuture(rateLimiter.tryAcquireAsync().toCompletableFuture()))
                .flatMap(acquired -> {
                    if (Boolean.TRUE.equals(acquired)) {
                        return chain.filter(exchange);
                    }
                    log.warn("RateLimiter triggered, key=[{}]", key);
                    return WebFluxUtils.webFluxResponseWriter(
                            exchange.getResponse(),
                            HttpStatus.TOO_MANY_REQUESTS,
                            R.fail(SystemErrorTypeEnum.TOO_MANY_REQUESTS));
                });
    }

    /**
     * 解析限流 Key。
     */
    private String resolveKey(ServerWebExchange exchange) {
        if (properties.getKeyType() == RateLimiterProperties.KeyType.USER) {
            try {
                Object loginId = cn.dev33.satoken.stp.StpUtil.getLoginId();
                return "user:" + loginId;
            } catch (Exception e) {
                log.debug("Failed to resolve user key for rate limiter, falling back to IP");
            }
        }
        // 默认：按 IP 限流
        String ip = exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
        return "ip:" + ip;
    }

    @Override
    public int getOrder() {
        return FilterOrder.RATE_LIMITER_FILTER;
    }
}
