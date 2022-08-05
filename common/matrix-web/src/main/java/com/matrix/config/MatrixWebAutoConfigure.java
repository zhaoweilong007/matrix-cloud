package com.matrix.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Lists;
import com.matrix.context.TenantContextHold;
import com.matrix.context.UserContextHolder;
import com.matrix.entity.vo.LoginUser;
import com.matrix.filter.XssFilter;
import com.matrix.properties.SecurityProperties;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

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

    public List<String> excludeList = Lists.newArrayList("/favicon.ico", "/actuator/**");

    @Bean
    public SaServletFilter getSaServletFilter(SecurityProperties securityProperties,
                                              TenantProperties tenantProperties) {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude()
                .setAuth(obj -> {
                    if (SaRouter.match(securityProperties.getWhiteUrls()).isHit || SaRouter.match(excludeList).isHit) {
                        //白名单放行
                        TenantContextHold.setIgnore(true);
                        return;
                    }
                    if (tenantProperties.getEnable() && SaRouter.match(tenantProperties.getIgnoreUrls()).isHit()) {
                        TenantContextHold.setIgnore(true);
                    }
                    // 校验 Id-Token 身份凭证
                    SaIdUtil.checkCurrentRequestToken();

                    LoginUser loginUser = LoginHelper.getLoginUser();
                    UserContextHolder.setLoginUser(loginUser);
                    if (loginUser.getTenantId() == null) {
                        TenantContextHold.setIgnore(true);
                    } else {
                        TenantContextHold.setTenantId(loginUser.getTenantId());
                    }
                })
                .setError(e -> SaResult.error(e.getMessage()));
    }
}
