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

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

    @Bean
    public I18nResponseBodyAdvice i18nReposeAdvice() {
        return new I18nResponseBodyAdvice();
    }
}
