package com.matrix.mq.redis.config;

import com.matrix.mq.redis.channel.AbstractRedisChannelMessageListener;
import com.matrix.mq.redis.core.RedisMqTemplate;
import com.matrix.mq.redis.core.job.RedisPendingMessageResendJob;
import com.matrix.mq.redis.core.job.RedisStreamMessageCleanupJob;
import com.matrix.mq.redis.stream.AbstractRedisStreamMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * Redis MQ Consumer 自动配置。
 *
 * <p>自动注册 Pub/Sub 监听器到 {@link RedisMessageListenerContainer}，
 * 以及 Stream 监听器到 {@link StreamMessageListenerContainer}。</p>
 *
 */
@Slf4j
@AutoConfiguration
@EnableScheduling
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnProperty(prefix = "matrix.mq.redis", name = "enabled", havingValue = "true")
public class RedisMqConsumerAutoConfiguration {

    /**
     * 创建 Redis Pub/Sub 广播消费的容器。
     */
    @Bean
    @ConditionalOnBean(AbstractRedisChannelMessageListener.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisMqTemplate redisMqTemplate,
            List<AbstractRedisChannelMessageListener<?>> listeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisMqTemplate.getRedisTemplate().getRequiredConnectionFactory());
        listeners.forEach(listener -> {
            listener.setRedisMqTemplate(redisMqTemplate);
            container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
            log.info("[RedisMQ] Registered Channel({}) listener: {}", listener.getChannel(), listener.getClass().getName());
        });
        return container;
    }

    /**
     * 创建 Redis Stream 集群消费的容器。
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnBean(AbstractRedisStreamMessageListener.class)
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
            RedisMqTemplate redisMqTemplate,
            List<AbstractRedisStreamMessageListener<?>> listeners) {
        StringRedisTemplate redisTemplate = redisMqTemplate.getRedisTemplate();

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .batchSize(10)
                        .targetType(String.class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                StreamMessageListenerContainer.create(redisTemplate.getRequiredConnectionFactory(), options);

        String consumerName = buildConsumerName();
        listeners.forEach(listener -> {
            log.info("[RedisMQ] Registering StreamKey({}) listener: {}", listener.getStreamKey(), listener.getClass().getName());
            try {
                redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
            } catch (Exception ignored) {
                // 消费者组已存在
            }
            listener.setRedisMqTemplate(redisMqTemplate);
            Consumer consumer = Consumer.from(listener.getGroup(), consumerName);
            StreamOffset<String> streamOffset = StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed());
            StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder =
                    StreamMessageListenerContainer.StreamReadRequest.builder(streamOffset)
                            .consumer(consumer)
                            .autoAcknowledge(false)
                            .cancelOnError(throwable -> false);
            container.register(builder.build(), listener);
            log.info("[RedisMQ] Registered StreamKey({}) listener: {}", listener.getStreamKey(), listener.getClass().getName());
        });
        return container;
    }

    private static String buildConsumerName() {
        return String.format("%s@%d",
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName(),
                Thread.currentThread().getId());
    }

    /**
     * Redis Stream 待处理消息重发 Job。
     */
    @Bean
    @ConditionalOnBean(AbstractRedisStreamMessageListener.class)
    public RedisPendingMessageResendJob redisPendingMessageResendJob(
            RedisMqTemplate redisMqTemplate,
            RedissonClient redissonClient,
            List<AbstractRedisStreamMessageListener<?>> listeners) {
        return new RedisPendingMessageResendJob(redisMqTemplate, redissonClient, listeners);
    }

    /**
     * Redis Stream 消息清理 Job。
     */
    @Bean
    @ConditionalOnBean(AbstractRedisStreamMessageListener.class)
    public RedisStreamMessageCleanupJob redisStreamMessageCleanupJob(
            RedisMqTemplate redisMqTemplate,
            RedissonClient redissonClient,
            List<AbstractRedisStreamMessageListener<?>> listeners) {
        return new RedisStreamMessageCleanupJob(redisMqTemplate, redissonClient, listeners);
    }
}
