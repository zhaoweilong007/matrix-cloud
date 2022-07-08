package com.matrix.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                    // 校验 Id-Token 身份凭证     —— 以下两句代码可简化为：SaIdUtil.checkCurrentRequestToken();
                    SaIdUtil.checkCurrentRequestToken();
                })
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}
