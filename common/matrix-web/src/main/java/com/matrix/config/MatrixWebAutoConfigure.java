package com.matrix.config;

import com.matrix.filter.XssFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 描述：sa-token配置
 *
 * @author zwl
 * @since 2022/7/8 15:50
 **/
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(MatrixTenantAutoConfiguration.class)
public class MatrixWebAutoConfigure {


    @Bean
    public SpringfoxHandlerProviderBeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new SpringfoxHandlerProviderBeanPostProcessor();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Integer.MIN_VALUE);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XssFilter());
        bean.setOrder(Integer.MIN_VALUE);
        return bean;
    }


}
