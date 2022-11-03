package com.matrix.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.matrix.component.TenantHandler;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Date;


/**
 * 描述：mybatis plus配置
 *
 * @author zwl
 * @since 2022/7/7 16:27
 **/
@EnableConfigurationProperties(TenantProperties.class)
@ConditionalOnProperty(value = "matrix.useDataSource", havingValue = "true")
@MapperScan("com.matrix.**.mapper")
public class MatrixMybatisAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantProperties tenantProperties) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        if (tenantProperties != null && tenantProperties.getEnable()) {
            TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new TenantHandler(tenantProperties));
            mybatisPlusInterceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }
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
}
