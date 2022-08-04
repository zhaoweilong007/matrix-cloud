package com.matrix.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.util.SaResult;
import com.matrix.filter.XssFilter;
import com.matrix.properties.SecurityProperties;
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

    @Bean
    public SaServletFilter getSaServletFilter(SecurityProperties securityProperties) {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico", "/actuator/**")
                .setAuth(obj -> {
                    if (SaRouter.match(securityProperties.getWhiteUrls()).isHit) {
                        //白名单放行
                        return;
                    }
                    // 校验 Id-Token 身份凭证
                    SaIdUtil.checkCurrentRequestToken();
                })
                .setError(e -> SaResult.error(e.getMessage()));
    }
}
