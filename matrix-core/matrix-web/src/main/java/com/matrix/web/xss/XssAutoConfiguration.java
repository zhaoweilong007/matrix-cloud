package com.matrix.web.xss;

import com.matrix.auto.properties.XssProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.module.SimpleModule;

/**
 * XSS 过滤自动配置
 *
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.xss", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(XssProperties.class)
public class XssAutoConfiguration implements WebMvcConfigurer {
    private final XssProperties properties;

    public XssAutoConfiguration(XssProperties properties) {
        this.properties = properties;
    }

    /**
     * 注册 XSS 过滤器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration(XssProperties properties) {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter(properties.getExcludeUrls()));
        registration.addUrlPatterns("/*");
        registration.setOrder(50);
        registration.setName("xssFilter");
        return registration;
    }

    @Bean
    public JsonMapperBuilderCustomizer xssJacksonCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new XssStringDeserializer());
            builder.addModule(module);
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new XssPathExcludeInterceptor(properties.getExcludeUrls()))
                .addPathPatterns("/**");
    }
}
