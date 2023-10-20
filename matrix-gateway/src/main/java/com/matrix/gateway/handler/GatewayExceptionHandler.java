package com.matrix.gateway.handler;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 */
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        R r;
        if (ex instanceof NotFoundException) {
            r = R.fail(SystemErrorTypeEnum.SERVICE_NOTE_FOUND);
        } else if (ex instanceof ResponseStatusException) {
            r = R.fail(SystemErrorTypeEnum.SYSTEM_ERROR);
        } else {
            r = R.fail(SystemErrorTypeEnum.FEIGN_INVOKE_ERROR);
        }
        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getURI(), ex.getMessage());
        return WebFluxUtils.webFluxResponseWriter(response, r);
    }
}
