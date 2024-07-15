package com.matrix.auth.config;

import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import com.matrix.auth.filter.LoginUserContextFilter;
import com.matrix.auth.filter.ValidateCodeFilter;
import com.matrix.auth.handler.AuthExceptionHandler;
import com.matrix.auto.properties.CaptchaProperties;
import com.matrix.common.constant.WebFilterOrderConstants;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.common.util.spring.ProfileUtils;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/8
 **/
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfiguration
@Slf4j
@EnableConfigurationProperties(CaptchaProperties.class)
public class SecurityConfiguration implements WebMvcConfigurer {

    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean<ValidateCodeFilter> validateCodeFilter(CaptchaProperties captchaProperties) {
        return createFilterBean(new ValidateCodeFilter(captchaProperties), WebFilterOrderConstants.VALIDATE_CODE_FILTER);
    }


    /**
     * 校验是否从网关转发
     */
    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setAuth(obj -> {
                    if (SaRouter.match("/actuator/**").isHit()) {
                        SaBasicUtil.check();
                        return;
                    }
                    if (ProfileUtils.isTest()) {
                        return;
                    }
                    SaSameUtil.checkCurrentRequestToken();
                })
                .setError(e -> {
                    log.warn("[SaServletFilter]认证失败,uri:[{}] msg:{}", ServletUtils.getRequest().getRequestURI(), e.getMessage());
                    return JsonUtil.toJson(R.fail(SystemErrorTypeEnum.UNAUTHORIZED));
                });
    }

    @Bean
    public LoginUserContextFilter securityFilter() {
        return new LoginUserContextFilter();
    }

    @Bean
    public AuthExceptionHandler authExceptionHandler() {
        return new AuthExceptionHandler();
    }


}
