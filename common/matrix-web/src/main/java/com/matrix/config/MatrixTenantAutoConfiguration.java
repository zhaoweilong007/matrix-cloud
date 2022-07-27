package com.matrix.config;

import cn.dev33.satoken.util.SaTokenConsts;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.google.common.collect.Lists;
import com.matrix.component.ReactorTenantHandler;
import com.matrix.component.ServletTenantHandler;
import com.matrix.filter.TenantFilter;
import com.matrix.properties.MatrixProperties;
import com.matrix.properties.TenantProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

/**
 * 描述：多租户配置
 *
 * @author zwl
 * @since 2022/7/26 14:52
 **/
public class MatrixTenantAutoConfiguration {


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<TenantFilter> tenantFilter() {
        FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantFilter());
        registrationBean.setOrder(SaTokenConsts.ASSEMBLY_ORDER + 1);
        return registrationBean;
    }


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public BeanPostProcessor mybatisPlusInterceptorBeanPostProcessor(MatrixProperties matrixProperties) {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof MybatisPlusInterceptor) {
                    TenantProperties tenantProperties = matrixProperties.getTenant();
                    if (tenantProperties != null && tenantProperties.getEnable()) {
                        MybatisPlusInterceptor plusInterceptor = (MybatisPlusInterceptor) bean;
                        ArrayList<InnerInterceptor> innerInterceptors = Lists.newArrayList(plusInterceptor.getInterceptors());
                        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new ServletTenantHandler(tenantProperties));
                        innerInterceptors.add(0, tenantLineInnerInterceptor);
                        plusInterceptor.setInterceptors(innerInterceptors);
                    }
                }
                return bean;
            }
        };
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public BeanPostProcessor mybatisPlusInterceptorBeanPostProcessor2(MatrixProperties matrixProperties) {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof MybatisPlusInterceptor) {
                    TenantProperties tenantProperties = matrixProperties.getTenant();
                    if (tenantProperties != null && tenantProperties.getEnable()) {
                        MybatisPlusInterceptor plusInterceptor = (MybatisPlusInterceptor) bean;
                        ArrayList<InnerInterceptor> innerInterceptors = Lists.newArrayList(plusInterceptor.getInterceptors());
                        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new ReactorTenantHandler(tenantProperties));
                        innerInterceptors.add(0, tenantLineInnerInterceptor);
                        plusInterceptor.setInterceptors(innerInterceptors);
                    }
                }
                return bean;
            }
        };
    }
}
