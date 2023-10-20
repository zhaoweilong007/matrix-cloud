package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义gateway参数配置
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "spring.cloud.gateway")
public class GatewayProperties {

    /**
     * 请求日志
     */
    private Boolean requestLog;


    /**
     * 允许跨域
     */
    private Set<String> allowOrigins = new HashSet<>();

}
