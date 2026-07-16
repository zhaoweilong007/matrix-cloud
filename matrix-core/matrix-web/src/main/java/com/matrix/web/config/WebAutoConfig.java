package com.matrix.web.config;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.matrix.common.constant.WebFilterOrderConstants;
import com.matrix.web.client.ApiAccessLogApi;
import com.matrix.web.exception.GlobalExceptionHandler;
import com.matrix.web.filter.ApiAccessLogFilter;
import com.matrix.web.filter.CacheRequestBodyFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 自动配置，注册 CORS、日志、XSS、异常处理等核心过滤器
 */
@AutoConfiguration
@EnableSpringUtil
public class WebAutoConfig implements WebMvcConfigurer {

    /**
     * 默认敏感词服务
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultSensitiveService defaultSensitiveService() {
        return new DefaultSensitiveService();
    }

    /**
     * CORS 跨域过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // 创建 CorsConfiguration 对象
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 设置访问源地址
        config.addAllowedHeader("*"); // 设置访问源请求头
        config.addAllowedMethod("*"); // 设置访问源请求方法
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
        return createFilterBean(new CorsFilter(source), WebFilterOrderConstants.CORS_FILTER);
    }


    /**
     * 创建 RequestBodyCacheFilter Bean，可重复读取请求内容
     */
    @Bean
    public FilterRegistrationBean<CacheRequestBodyFilter> requestBodyCacheFilter() {
        return createFilterBean(new CacheRequestBodyFilter(), WebFilterOrderConstants.REQUEST_BODY_CACHE_FILTER);
    }


    /**
     * 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


    /**
     * 创建 ApiAccessLogFilter Bean，记录 API 请求日志
     */
    @Bean
    @ConditionalOnProperty(prefix = "matrix.access-log", value = "enable", matchIfMissing = true)
    public FilterRegistrationBean<ApiAccessLogFilter> apiAccessLogFilter(
        @Value("${spring.application.name}") String applicationName,
        ApiAccessLogApi apiAccessLogApi) {
        ApiAccessLogFilter filter = new ApiAccessLogFilter(applicationName, apiAccessLogApi);
        return createFilterBean(filter, WebFilterOrderConstants.API_ACCESS_LOG_FILTER);
    }


    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }
}
