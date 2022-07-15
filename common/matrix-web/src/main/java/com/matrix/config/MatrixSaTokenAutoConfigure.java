package com.matrix.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.util.SaResult;
import com.matrix.component.FeignInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 描述：sa-token配置
 *
 * @author zwl
 * @since 2022/7/8 15:50
 **/
@ConditionalOnClass(WebMvcAutoConfiguration.class)
public class MatrixSaTokenAutoConfigure {

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico", "/actuator/**")
                .setAuth(obj -> {
                    SaIdUtil.checkCurrentRequestToken();
                })
                .setError(e -> SaResult.error(e.getMessage()).setCode(401));
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
