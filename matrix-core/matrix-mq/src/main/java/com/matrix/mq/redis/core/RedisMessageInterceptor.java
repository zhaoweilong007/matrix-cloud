package com.matrix.mq.redis.core;

import com.matrix.mq.redis.message.AbstractRedisMessage;

/**
 * Redis 消息拦截器 SPI。
 * 可在消息发送/消费前后插入逻辑（如租户上下文传播、日志记录等）。
 *
 * @author matrix
 */
public interface RedisMessageInterceptor {

    /** 发送消息前 */
    default void sendMessageBefore(AbstractRedisMessage message) {}

    /** 发送消息后 */
    default void sendMessageAfter(AbstractRedisMessage message) {}

    /** 消费消息前 */
    default void consumeMessageBefore(AbstractRedisMessage message) {}

    /** 消费消息后 */
    default void consumeMessageAfter(AbstractRedisMessage message) {}
}
