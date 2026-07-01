package com.matrix.web.xss;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * XSS 过滤自动配置
 *
 * @author matrix
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.xss", value = "enabled", havingValue = "true")
public class XssAutoConfiguration {

    @Bean
    public XssProperties xssProperties() {
        return new XssProperties();
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration(XssProperties properties) {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter(properties.getExcludeUrls()));
        registration.addUrlPatterns("/*");
        registration.setOrder(50);
        registration.setName("xssFilter");
        return registration;
    }

    /**
     * XSS 配置属性
     */
    @Component
    @ConfigurationProperties(prefix = "matrix.xss")
    public static class XssProperties {
        /**
         * 排除过滤的 URL 模式列表
         */
        private List<String> excludeUrls = Collections.emptyList();

        public List<String> getExcludeUrls() {
            return excludeUrls;
        }

        public void setExcludeUrls(List<String> excludeUrls) {
            this.excludeUrls = excludeUrls;
        }
    }
}
