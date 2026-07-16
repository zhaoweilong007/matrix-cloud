package com.matrix.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.enums.PlatformUserTypeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关请求头清洗与上下文传播过滤器。
 * <p>1. 清理客户端传入的任何伪造头（如 user_id、user_type、login-user）。</p>
 * <p>2. 对已认证的用户，向下游传递安全的 user_id 和 user_type 上下文头。</p>
 */
@Component
public class RequestHeaderCleanFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 1. 强力清洗所有可能的外部伪造头，防止穿透攻击
        mutate.header("login-user", (String[]) null)
              .header("login-user-id", (String[]) null)
              .header("login-user-type", (String[]) null)
              .header(CommonConstants.USER_ID_HEADER, (String[]) null)
              .header(CommonConstants.USER_TYPE, (String[]) null)
              .header(CommonConstants.USER_MOBILE_HEADER, (String[]) null);

        // 2. 如果用户已登录，向微服务传播网关鉴权获得的身份头
        if (StpUtil.isLogin()) {
            mutate.header(CommonConstants.USER_ID_HEADER, StpUtil.getLoginIdAsString());
            mutate.header(CommonConstants.USER_TYPE, PlatformUserTypeEnum.SYS_USER.name());
        }

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        // 在网关鉴权之后，但在路由负载均衡转发之前运行
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
