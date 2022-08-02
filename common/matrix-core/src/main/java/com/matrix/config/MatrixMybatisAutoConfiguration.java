package com.matrix.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.google.common.collect.Lists;
import com.matrix.component.TenantHandler;
import com.matrix.properties.MatrixProperties;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;


/**
 * 描述：mybatis plus配置
 *
 * @author zwl
 * @since 2022/7/7 16:27
 **/
@EnableConfigurationProperties(TenantProperties.class)
public class MatrixMybatisAutoConfiguration {

    @Bean
    @ConditionalOnBean(MatrixProperties.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }


    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        // 自动填充参数类
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                setFieldValByName("createBy", LoginHelper.getUsername(), metaObject);
                setFieldValByName("createTime", new Date(), metaObject);
                this.updateFill(metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                setFieldValByName("updateBy", LoginHelper.getUsername(), metaObject);
                setFieldValByName("updateTime", new Date(), metaObject);
            }
        };
    }

    @Bean
    public BeanPostProcessor mybatisPlusInterceptorBeanPostProcessor(MatrixProperties matrixProperties) {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof MybatisPlusInterceptor) {
                    TenantProperties tenantProperties = matrixProperties.getTenant();
                    if (tenantProperties != null && tenantProperties.getEnable()) {
                        MybatisPlusInterceptor plusInterceptor = (MybatisPlusInterceptor) bean;
                        ArrayList<InnerInterceptor> innerInterceptors = Lists.newArrayList(plusInterceptor.getInterceptors());
                        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new TenantHandler(tenantProperties));
                        innerInterceptors.add(0, tenantLineInnerInterceptor);
                        plusInterceptor.setInterceptors(innerInterceptors);
                    }
                }
                return bean;
            }
        };
    }

}
