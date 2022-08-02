package com.matrix.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import com.matrix.component.FeignInterceptor;
import com.matrix.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/29 16:38
 **/
@EnableConfigurationProperties(SecurityProperties.class)
public class MatrixSaTokenAutoConfiguration {

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
