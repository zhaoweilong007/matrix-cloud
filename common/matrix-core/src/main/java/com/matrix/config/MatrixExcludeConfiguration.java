package com.matrix.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/3 11:08
 **/
public class MatrixExcludeConfiguration {

    @ConditionalOnProperty(value = "matrix.useDataSource", havingValue = "false", matchIfMissing = true)
    @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
    @Configuration(proxyBeanMethods = false)
    public static class DataSourceExcludeConfiguration {

    }
}
