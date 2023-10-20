package com.matrix.gateway.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import com.matrix.auto.properties.IgnoreWhiteProperties;
import com.matrix.auto.properties.TenantAuthProperties;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.gateway.filter.BlackListUrlFilter;
import com.matrix.gateway.filter.ForwardAuthFilter;
import com.matrix.gateway.filter.SecurityAuthStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhaoWeiLong
 * @since 2023/9/18
 **/
@Configuration
@EnableConfigurationProperties({IgnoreWhiteProperties.class, TenantAuthProperties.class})
public class SecurityConfig {


    @Bean
    public SaReactorFilter saReactorFilter(SecurityAuthStrategy securityAuthStrategy) {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 鉴权方法：每次访问进入
                .setAuth(securityAuthStrategy)
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        return R.fail(SystemErrorTypeEnum.UNAUTHORIZED, e.getMessage());
                    }
                    return R.fail(SystemErrorTypeEnum.UNAUTHORIZED);
                });
    }


    @Bean
    public BlackListUrlFilter blackListUrlFilter() {
        return new BlackListUrlFilter();
    }

    @Bean
    public ForwardAuthFilter forwardAuthFilter() {
        return new ForwardAuthFilter();
    }


    @Bean
    public SecurityAuthStrategy securityAuthStrategy(IgnoreWhiteProperties ignoreWhite) {
        return new SecurityAuthStrategy(ignoreWhite);
    }
}
