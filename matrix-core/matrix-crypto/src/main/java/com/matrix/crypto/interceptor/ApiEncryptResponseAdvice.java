package com.matrix.crypto.interceptor;

import com.matrix.crypto.annotation.ApiEncrypt;
import com.matrix.crypto.service.CryptoService;
import com.matrix.common.result.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * API 响应加密拦截器
 *
 * @author matrix
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiEncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    private final CryptoService cryptoService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查方法或类上是否有 @ApiEncrypt 注解
        ApiEncrypt annotation = returnType.getMethodAnnotation(ApiEncrypt.class);
        if (annotation == null) {
            annotation = returnType.getContainingClass().getAnnotation(ApiEncrypt.class);
        }
        return annotation != null && annotation.encryptResponse();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof R<?> r) {
            try {
                // 加密 data 字段
                if (r.getData() != null) {
                    String json = new com.fasterxml.jackson.databind.ObjectMapper()
                            .writeValueAsString(r.getData());
                    String encrypted = cryptoService.encrypt(json);
                    @SuppressWarnings("unchecked")
                    R<Object> raw = (R<Object>) (R<?>) r;
                    raw.setData(encrypted);
                }
                return r;
            } catch (Exception e) {
                log.error("响应加密失败: {}", e.getMessage(), e);
                return body;
            }
        }
        return body;
    }
}
