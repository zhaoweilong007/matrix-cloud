package com.matrix.feign.config;

import cn.hutool.core.util.StrUtil;
import com.matrix.common.constant.ConfigConstants;
import com.matrix.common.constant.Constants;
import net.dreamlu.mica.auto.annotation.AutoEnvPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 版本环境配置
 *
 * @author ZhaoWeiLong
 * @since 2023/9/5
 **/
@AutoEnvPostProcessor
public class VersionEnvironment implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final String version = environment.getProperty(Constants.VERSION);
        //不存在时 设置为默认版本号
        if (StrUtil.isBlank(version)) {
            final String defaultVersion = environment.getProperty(ConfigConstants.CONFIG_LOADBALANCE_ISOLATION_DEFAULT_VERSION);
            environment.getSystemProperties().put("spring.cloud.nacos.discovery.metadata.version", defaultVersion != null ? defaultVersion : Constants.DEFAULT_VERSION);
        } else {
            environment.getSystemProperties().put("spring.cloud.nacos.discovery.metadata.version", version);
        }
    }
}
