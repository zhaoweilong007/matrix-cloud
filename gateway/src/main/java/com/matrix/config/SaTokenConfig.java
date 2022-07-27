package com.matrix.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaResult;
import com.matrix.filter.ForwardAuthFilter;
import com.matrix.security.SecurityAuth;
import com.matrix.security.SecurityBeforeAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 描述：sa-token配置
 *
 * @author zwl
 * @since 2022/7/8 14:48
 **/
@Configuration(proxyBeanMethods = false)
public class SaTokenConfig {

    /**
     * Sa-Token 整合 jwt (简单模式)
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 为请求添加 Id-Token
     */
    @Bean
    public ForwardAuthFilter forwardAuthFilter(MatrixSecurityProperties matrixSecurityProperties) {
        return new ForwardAuthFilter(matrixSecurityProperties);
    }

    /**
     * 注册 Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter(SecurityAuth securityAuth,
                                              SecurityBeforeAuth securityBeforeAuth,
                                              MatrixSecurityProperties matrixSecurityProperties) {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico", "/actuator/**")
                .addExclude(matrixSecurityProperties.getWhiteUrls().toArray(new String[0]))
                .setBeforeAuth(securityBeforeAuth)
                .setAuth(securityAuth)
                .setError(e -> SaResult.error(e.getMessage()).setCode(401));
    }

}
