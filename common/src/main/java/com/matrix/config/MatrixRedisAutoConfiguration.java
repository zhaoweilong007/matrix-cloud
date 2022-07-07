package com.matrix.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * 描述： redis配置
 *
 * @author zwl
 * @since 2022/7/7 13:59
 **/
@EnableCaching
public class MatrixRedisAutoConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }


    /**
     * todo redis cache自定义配置，通过matrix.cache配置自定义多个cacheName
     */
    @Bean
    @ConditionalOnBean(MatrixConfiguration.class)
    public BeanPostProcessor cacheManagerProcessor(RedisConnectionFactory factory, MatrixConfiguration matrixConfiguration) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof RedisCacheManager) {
                    List<CacheProperties> cache = matrixConfiguration.getCache();
                    if (CollectionUtil.isNotEmpty(cache)) {
                        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.RedisCacheManagerBuilder
                                .fromConnectionFactory(factory)
                                .enableStatistics()
                                .transactionAware();
                        //支持多cacheName不同的缓存配置
                        cache.forEach(cacheProperties -> {
                            redisCacheManagerBuilder.withCacheConfiguration(cacheProperties.getName(), getCacheConfigurationWithTtl(cacheProperties));
                        });
                        return redisCacheManagerBuilder.build();
                    } else {
                        return bean;
                    }
                }
                return bean;
            }
        };

    }

    private RedisCacheConfiguration getCacheConfigurationWithTtl(CacheProperties cacheProperties) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                .disableCachingNullValues()
                .prefixCacheNameWith(cacheProperties.getPrefix())
                .entryTtl(cacheProperties.getTtl());
    }


}
