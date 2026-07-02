package com.matrix.redis.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.redis.config.properties.RedissonProperties;
import com.matrix.redis.handler.KeyPrefixHandler;
import com.matrix.redis.manager.PlusSpringCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis配置
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {

    /** Redisson 配置属性 */
    @Autowired
    private RedissonProperties redissonProperties;

    /** Jackson ObjectMapper 实例 */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 自定义 Redisson 自动配置的 RedisTemplate，使用 JSON 序列化方式。
     * Redisson 4.x AutoConfigurationV4 已注册 redisTemplate，此处通过 BeanPostProcessor 定制序列化。
     */
    @Bean
    public static BeanPostProcessor redisTemplatePostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof RedisTemplate<?, ?> template) {
                    template.setKeySerializer(RedisSerializer.string());
                    template.setHashKeySerializer(RedisSerializer.string());
                    template.setValueSerializer(RedisSerializer.json());
                    template.setHashValueSerializer(RedisSerializer.json());
                }
                return bean;
            }
        };
    }

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return config -> {
            config.setThreads(redissonProperties.getThreads())
                    .setNettyThreads(redissonProperties.getNettyThreads())
                    .setCodec(new JsonJacksonCodec(objectMapper));
            RedissonProperties.SingleServerConfig singleServerConfigLocal = redissonProperties.getSingleServerConfig();
            if (ObjectUtil.isNotNull(singleServerConfigLocal) && singleServerConfigLocal.getEnable()) {
                // 使用单机模式
                SingleServerConfig singleServer = config.useSingleServer();
                BeanUtil.copyProperties(
                        singleServerConfigLocal,
                        singleServer,
                        CopyOptions.create().ignoreNullValue());
                singleServer.setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()));
                return;
            }
            // 主从模式配置
            RedissonProperties.MasterSlaveConfig masterSlaveConfig = redissonProperties.getMasterSlaveServersConfig();
            if (ObjectUtil.isNotNull(masterSlaveConfig) && masterSlaveConfig.getEnable()) {
                MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
                BeanUtil.copyProperties(
                        masterSlaveConfig,
                        masterSlaveServersConfig,
                        CopyOptions.create().ignoreNullValue());
                return;
            }

            // 集群配置方式 参考下方注释
            RedissonProperties.ClusterConfig clusterServersConfig = redissonProperties.getClusterServersConfig();
            if (ObjectUtil.isNotNull(clusterServersConfig) && clusterServersConfig.getEnable()) {
                ClusterServersConfig clusterServers = config.useClusterServers();
                BeanUtil.copyProperties(
                        clusterServersConfig,
                        clusterServers,
                        CopyOptions.create().ignoreNullValue());
                return;
            }
            log.info("初始化 redis 配置");
        };
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }
}
