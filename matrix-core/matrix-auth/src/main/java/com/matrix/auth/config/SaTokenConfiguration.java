package com.matrix.auth.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.matrix.auth.core.dao.PlusSaTokenDao;
import com.matrix.auth.core.service.SaPermissionImpl;
import com.matrix.auth.sign.ApiSignatureProperties;
import com.matrix.auth.sign.AppSecretProvider;
import com.matrix.auth.sign.ConfigAppSecretProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Sa-Token 配置
 */
@AutoConfiguration
public class SaTokenConfiguration {

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new PlusSaTokenDao();
    }

    /**
     * 默认 AppSecret 提供者（从配置文件读取）。
     * 业务方可实现 {@link AppSecretProvider} 并注册为 Spring Bean 以替换此默认实现。
     */
    @Bean
    @ConditionalOnMissingBean(AppSecretProvider.class)
    public AppSecretProvider configAppSecretProvider(ApiSignatureProperties properties) {
        return new ConfigAppSecretProvider(properties);
    }
}
