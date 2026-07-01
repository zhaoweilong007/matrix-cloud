package com.matrix.gateway.service;

import com.matrix.common.result.R;
import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * 验证码处理
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    Mono<R<Map<String, String>>> createCaptcha();
}
