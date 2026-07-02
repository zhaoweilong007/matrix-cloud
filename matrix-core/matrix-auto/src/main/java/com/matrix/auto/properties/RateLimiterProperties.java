package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 网关限流配置属性。
 *
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "matrix.rate-limiter")
public class RateLimiterProperties {

    /**
     * 是否启用网关限流
     */
    private boolean enabled = false;

    /**
     * 令牌桶填充速率（每秒补充的令牌数）
     */
    private int replenishRate = 10;

    /**
     * 令牌桶容量（突发请求上限）
     */
    private int burstCapacity = 20;

    /**
     * Key 解析策略
     */
    private KeyType keyType = KeyType.IP;

    public enum KeyType {
        /**
         * 按客户端 IP 限流
         */
        IP,
        /**
         * 按登录用户 ID 限流（需已认证的请求）
         */
        USER
    }
}
