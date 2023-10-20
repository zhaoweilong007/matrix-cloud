package com.matrix.gateway.handler;

import com.matrix.gateway.service.ValidateCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 验证码获取
 */
@RequiredArgsConstructor
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {

    private final ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return validateCodeService.createCaptcha()
                .onErrorResume(Mono::error)
                .flatMap(ajax -> ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(ajax)));
    }
}
