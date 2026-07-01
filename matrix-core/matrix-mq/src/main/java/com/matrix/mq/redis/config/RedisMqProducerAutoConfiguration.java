package com.matrix.mq.redis.config;

import com.matrix.mq.redis.core.RedisMessageInterceptor;
import com.matrix.mq.redis.core.RedisMqTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Redis MQ Producer 自动配置。
 *
 * @author matrix
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnProperty(prefix = "matrix.mq.redis", name = "enabled", havingValue = "true")
public class RedisMqProducerAutoConfiguration {

    @Bean
    public RedisMqTemplate redisMqTemplate(StringRedisTemplate redisTemplate,
                                           List<RedisMessageInterceptor> interceptors) {
        RedisMqTemplate redisMqTemplate = new RedisMqTemplate(redisTemplate);
        interceptors.forEach(redisMqTemplate.getInterceptors()::add);
        log.info("Redis MQ Producer initialized with {} interceptor(s)", interceptors.size());
        return redisMqTemplate;
    }
}
