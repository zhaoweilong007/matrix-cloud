package com.matrix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.matrix.component.CacheManagerProcessor;
import com.matrix.component.RedisOps;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 描述： redis配置
 *
 * @author zwl
 * @since 2022/7/7 13:59
 **/
@EnableCaching
@Import(CacheManagerProcessor.class)
public class MatrixRedisAutoConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
        RedisSerializer<Object> json = new GenericJackson2JsonRedisSerializer(objectMapper);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        template.setValueSerializer(json);
        template.setHashValueSerializer(json);
        return template;
    }

    @Bean
    public RedisOps redisOps(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisOps(redisTemplate, stringRedisTemplate, true);
    }
}
