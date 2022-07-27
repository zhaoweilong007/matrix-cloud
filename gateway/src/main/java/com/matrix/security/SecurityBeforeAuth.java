package com.matrix.security;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/16 11:12
 **/
@Slf4j
@Component
public class SecurityBeforeAuth implements SaFilterAuthStrategy {
    @Override
    public void run(Object r) {
        ServerHttpRequest request = SaReactorSyncHolder.getContext().getRequest();
        log.info("SecurityBeforeAuth: {}", request.getPath().value());
    }
}
