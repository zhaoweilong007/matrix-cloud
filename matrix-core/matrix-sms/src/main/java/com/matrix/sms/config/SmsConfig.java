package com.matrix.sms.config;

import com.matrix.redis.utils.RedisUtils;
import com.matrix.sms.core.SmsRedisDao;
import com.matrix.sms.handler.SmsExceptionHandler;
import com.matrix.sms.properties.SmsProperties;
import org.dromara.sms4j.api.dao.SmsDao;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * SMS 短信模块自动配置。
 *
 **/
@AutoConfiguration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {

    /**
     * Redis 缓存的 SmsDao，支持短信重试和发送间隔限流。
     */
    @Primary
    @Bean
    @ConditionalOnClass(RedisUtils.class)
    public SmsDao smsDao() {
        return new SmsRedisDao();
    }

    /**
     * 全局 SMS 异常处理器。
     */
    @Bean
    public SmsExceptionHandler smsExceptionHandler() {
        return new SmsExceptionHandler();
    }
}
