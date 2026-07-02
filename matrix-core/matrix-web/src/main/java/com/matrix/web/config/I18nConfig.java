package com.matrix.web.config;

import com.matrix.web.handler.I18nLocaleResolver;
import com.matrix.web.handler.I18nResponseBodyAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置
 */
@AutoConfiguration
public class I18nConfig {

    /**
     * 创建国际化区域解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

    /**
     * 创建国际化响应体增强处理器
     */
    @Bean
    public I18nResponseBodyAdvice i18nReposeAdvice() {
        return new I18nResponseBodyAdvice();
    }
}
