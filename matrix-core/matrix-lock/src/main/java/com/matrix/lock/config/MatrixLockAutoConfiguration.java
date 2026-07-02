package com.matrix.lock.config;

import com.matrix.lock.core.DefaultLockFailureStrategy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 分布式锁自动配置。
 *
 * <p>在 Lock4j 自身初始化之前注册自定义 {@link DefaultLockFailureStrategy}，
 * 使获取锁失败时抛出 {@link com.matrix.common.exception.ServiceException}
 * 而非默认的 {@link com.baomidou.lock.exception.LockFailureException}。</p>
 *
 */
@AutoConfiguration(before = com.baomidou.lock.spring.boot.autoconfigure.LockAutoConfiguration.class)
public class MatrixLockAutoConfiguration {

    @Bean
    public DefaultLockFailureStrategy defaultLockFailureStrategy() {
        return new DefaultLockFailureStrategy();
    }
}
