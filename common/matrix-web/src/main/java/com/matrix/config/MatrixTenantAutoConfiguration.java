package com.matrix.config;

import cn.dev33.satoken.util.SaTokenConsts;
import com.matrix.exception.GlobalExceptionHandler;
import com.matrix.filter.TenantServletFilter;
import com.matrix.properties.SecurityProperties;
import com.matrix.properties.TenantProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 描述：多租户配置
 *
 * @author zwl
 * @since 2022/7/26 14:52
 **/
public class MatrixTenantAutoConfiguration {
    @Bean
    public FilterRegistrationBean<TenantServletFilter> tenantFilter(TenantProperties tenantProperties, SecurityProperties securityProperties, GlobalExceptionHandler globalExceptionHandler) {
        FilterRegistrationBean<TenantServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantServletFilter(tenantProperties,securityProperties,globalExceptionHandler));
        registrationBean.setOrder(SaTokenConsts.ASSEMBLY_ORDER + 1);
        return registrationBean;
    }

}
