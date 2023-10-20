package com.matrix.tenant.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.matrix.auto.properties.TenantProperties;
import com.matrix.common.constant.WebFilterOrderConstants;
import com.matrix.mybatis.utils.MyBatisUtils;
import com.matrix.tenant.api.client.ITenantApi;
import com.matrix.tenant.core.aop.TenantIgnoreAspect;
import com.matrix.tenant.core.db.TenantDatabaseInterceptor;
import com.matrix.tenant.core.job.TenantJobAspect;
import com.matrix.tenant.core.redis.TenantRedisCacheManager;
import com.matrix.tenant.core.security.TenantSecurityWebFilter;
import com.matrix.tenant.core.service.ITenantFrameworkService;
import com.matrix.tenant.core.service.TenantFrameworkServiceImpl;
import com.matrix.tenant.core.web.TenantContextWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.tenant", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfiguration {

    @Bean
    public ITenantFrameworkService tenantFrameworkService(ITenantApi tenantApi) {
        return new TenantFrameworkServiceImpl(tenantApi);
    }

    // ========== AOP ==========

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                                                                 MybatisPlusInterceptor interceptor) {
        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(properties));
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    // ========== WEB ==========

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(WebFilterOrderConstants.TENANT_CONTEXT_FILTER);
        return registrationBean;
    }

    // ========== Security ==========

    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   ITenantFrameworkService tenantFrameworkService) {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, tenantFrameworkService));
        registrationBean.setOrder(WebFilterOrderConstants.TENANT_SECURITY_FILTER);
        return registrationBean;
    }

    // ========== Job ==========

    @Bean
    @ConditionalOnClass(name = "com.xxl.job.core.handler.annotation.XxlJob")
    public TenantJobAspect tenantJobAspect(ITenantFrameworkService tenantFrameworkService) {
        return new TenantJobAspect(tenantFrameworkService);
    }

    // ========== Redis ==========

    @Bean
    @Primary // 引入租户时，tenantRedisCacheManager 为主 Bean
    public TenantRedisCacheManager tenantRedisCacheManager() {
        // 创建 TenantRedisCacheManager 对象
        return new TenantRedisCacheManager();
    }

}
