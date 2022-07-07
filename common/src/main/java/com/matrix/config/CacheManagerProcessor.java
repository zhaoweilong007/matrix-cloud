package com.matrix.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述： 自定义缓存配置
 *
 * @author zwl
 * @since 2022/7/7 18:03
 **/
@Component
public class CacheManagerProcessor implements BeanPostProcessor {

    private final RedisConnectionFactory factory;
    private final MatrixConfiguration matrixConfiguration;

    @Lazy
    public CacheManagerProcessor(RedisConnectionFactory factory, MatrixConfiguration matrixConfiguration) {
        this.factory = factory;
        this.matrixConfiguration = matrixConfiguration;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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
            }
        }
        return bean;
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
