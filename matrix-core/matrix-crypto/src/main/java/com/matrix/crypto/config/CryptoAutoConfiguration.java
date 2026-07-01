package com.matrix.crypto.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.crypto.filter.ApiEncryptRequestFilter;
import com.matrix.crypto.interceptor.ApiEncryptResponseAdvice;
import com.matrix.crypto.service.AesCryptoService;
import com.matrix.crypto.service.CryptoService;
import com.matrix.crypto.service.RsaCryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * API 加解密自动配置
 *
 * @author matrix
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.crypto", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(CryptoProperties.class)
@RequiredArgsConstructor
public class CryptoAutoConfiguration {

    private final CryptoProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public CryptoService cryptoService() {
        if (properties.getType() == CryptoProperties.CryptoType.RSA) {
            return new RsaCryptoService(properties.getPublicKey(), properties.getPrivateKey());
        }
        return new AesCryptoService(properties.getSecretKey());
    }

    @Bean
    public ApiEncryptResponseAdvice apiEncryptResponseAdvice(CryptoService cryptoService) {
        return new ApiEncryptResponseAdvice(cryptoService);
    }

    @Bean
    public FilterRegistrationBean<ApiEncryptRequestFilter> apiEncryptRequestFilter(
            CryptoService cryptoService, ObjectMapper objectMapper,
            RequestMappingHandlerMapping handlerMapping) {
        FilterRegistrationBean<ApiEncryptRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiEncryptRequestFilter(cryptoService, properties, objectMapper, handlerMapping));
        registration.addUrlPatterns("/*");
        registration.setOrder(100);
        return registration;
    }
}
