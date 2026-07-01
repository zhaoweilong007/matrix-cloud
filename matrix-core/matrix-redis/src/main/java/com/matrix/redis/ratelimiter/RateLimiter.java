package com.matrix.redis.ratelimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis 令牌桶限流注解
 * <p>
 * 支持 5 种 Key 解析策略：全局、用户、IP、服务节点、SpEL 表达式
 * </p>
 *
 * @author matrix
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * 每秒允许的请求数（令牌桶填充速率）
     */
    double permitsPerSecond() default 10;

    /**
     * 最大等待时间，超时则拒绝（秒）
     */
    long timeout() default 1;

    /**
     * 限流提示消息
     */
    String message() default "请求过于频繁，请稍后再试";

    /**
     * 限流 Key 解析器类型
     */
    KeyType keyType() default KeyType.USER;

    /**
     * 自定义 key（当 keyType = CUSTOM 时使用 SpEL 表达式）
     */
    String key() default "";

    enum KeyType {
        /**
         * 全局级别（所有请求共享令牌桶）
         */
        GLOBAL,
        /**
         * 用户级别（每个用户单独限流）
         */
        USER,
        /**
         * IP 级别（每个 IP 单独限流）
         */
        IP,
        /**
         * 服务节点级别（每个实例单独限流）
         */
        SERVER_NODE,
        /**
         * 自定义（通过 SpEL 表达式自定义 key）
         */
        CUSTOM
    }
}
