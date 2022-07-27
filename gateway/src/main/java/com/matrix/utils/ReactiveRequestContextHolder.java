package com.matrix.utils;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import java.util.Objects;

/**
 * 描述：tenant context holder
 *
 * @author zwl
 * @since 2022/7/16 10:24
 **/
public class ReactiveRequestContextHolder {
    public static Mono<ServerWebExchange> getExchange() {
        return Mono.deferContextual(contextView -> {
            ServerWebExchange exchange = contextView.get(CONTEXT_KEY);
            return Mono.just(exchange);
        });
    }

    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;



    /**
     * Gets the {@code Mono<ServerHttpRequest>} from Reactor {@link Context}
     *
     * @return the {@code Mono<ServerHttpRequest>}
     */
    public static Mono<ServerHttpRequest> getRequest() {
        return ReactiveRequestContextHolder.getExchange()
                .map(ServerWebExchange::getRequest);
    }

    /**
     * Put the {@code ServerWebExchange} to Reactor {@link Context}
     *
     * @param context  Context
     * @param exchange ServerWebExchange
     * @return the Reactor {@link Context}
     */
    public static Context put(Context context, ServerWebExchange exchange) {
        return context.put(CONTEXT_KEY, exchange);
    }

    private static final ThreadLocal<ServerHttpRequest> requests = new NamedThreadLocal<>("Thread ServerHttpRequest");

    /**
     * Store {@link ServerHttpRequest} to {@link ThreadLocal} in the current thread
     *
     * @param request {@link ServerHttpRequest}
     */
    public static void put(ServerHttpRequest request) {

        //When the request time out, the reset will be invalid
        //because the timeout thread is the thread of hystrix, not the thread of HTTP
        if (Objects.nonNull(get())) {
            reset();
        }

        if (request != null) {
            requests.set(request);
        }
    }

    /**
     * Get the current thread {@link ServerHttpRequest} from {@link ThreadLocal}
     *
     * @return {@link ServerHttpRequest}
     */
    public static ServerHttpRequest get() {
        ServerHttpRequest request = requests.get();
        return request;
    }

    /**
     * Clear the current thread {@link ServerHttpRequest} from {@link ThreadLocal}
     */
    public static void reset() {
        requests.remove();
    }
}
