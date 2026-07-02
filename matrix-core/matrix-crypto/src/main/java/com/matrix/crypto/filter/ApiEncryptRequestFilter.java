package com.matrix.crypto.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.crypto.annotation.ApiEncrypt;
import com.matrix.crypto.config.CryptoProperties;
import com.matrix.crypto.service.CryptoService;
import com.matrix.crypto.util.CryptoRequestBodyWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * API 请求解密过滤器
 *
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@RequiredArgsConstructor
public class ApiEncryptRequestFilter extends OncePerRequestFilter {

    /**
     * 加解密服务
     */
    private final CryptoService cryptoService;

    /**
     * 加解密配置属性
     */
    private final CryptoProperties properties;

    /**
     * JSON 对象映射器
     */
    private final ObjectMapper objectMapper;

    /**
     * 处理器映射器，用于获取 @ApiEncrypt 注解
     */
    private final RequestMappingHandlerMapping handlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 判断是否需要解密
        if (!needDecrypt(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 包装请求，替换请求体
        CryptoRequestBodyWrapper wrapper = new CryptoRequestBodyWrapper(request);
        try {
            String body = new String(request.getInputStream().readAllBytes(), request.getCharacterEncoding());
            if (StringUtils.hasText(body)) {
                JsonNode jsonNode = objectMapper.readTree(body);
                String encryptData = jsonNode.has(properties.getEncryptField())
                        ? jsonNode.get(properties.getEncryptField()).asText()
                        : null;
                if (StringUtils.hasText(encryptData)) {
                    String decrypted = cryptoService.decrypt(encryptData);
                    wrapper.setBody(decrypted.getBytes(request.getCharacterEncoding()));
                }
            }
        } catch (Exception e) {
            log.error("请求解密失败: {}", e.getMessage(), e);
            throw new RuntimeException("请求解密失败", e);
        }

        filterChain.doFilter(wrapper, response);
    }

    /**
     * 判断当前请求是否需要解密
     */
    private boolean needDecrypt(HttpServletRequest request) {
        // 判断请求类型
        String contentType = request.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return false;
        }

        // 判断是否有对应的 HandlerMethod
        try {
            HandlerMethod handlerMethod = getHandlerMethod(request);
            if (handlerMethod == null) {
                return false;
            }

            // 方法或类上有 @ApiEncrypt 注解
            ApiEncrypt annotation = handlerMethod.getMethodAnnotation(ApiEncrypt.class);
            if (annotation == null) {
                annotation = handlerMethod.getBeanType().getAnnotation(ApiEncrypt.class);
            }

            return annotation != null && annotation.decryptRequest();
        } catch (Exception e) {
            log.debug("获取HandlerMethod失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取请求对应的 HandlerMethod
     */
    private HandlerMethod getHandlerMethod(HttpServletRequest request) {
        try {
            Object handler = handlerMapping.getHandler(request).getHandler();
            return handler instanceof HandlerMethod ? (HandlerMethod) handler : null;
        } catch (Exception e) {
            return null;
        }
    }
}
