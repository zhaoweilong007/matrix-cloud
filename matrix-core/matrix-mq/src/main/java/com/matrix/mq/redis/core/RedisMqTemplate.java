package com.matrix.mq.redis.core;

import com.matrix.common.util.json.JsonUtils;
import com.matrix.mq.redis.channel.AbstractRedisChannelMessage;
import com.matrix.mq.redis.message.AbstractRedisMessage;
import com.matrix.mq.redis.stream.AbstractRedisStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis MQ 操作模板，同时支持 Pub/Sub 和 Stream 两种模式。
 *
 * <p>使用示例：</p>
 * <pre>
 * // 发送 Pub/Sub 消息
 * redisMqTemplate.send(new OrderPaidMessage().setOrderId(1L));
 *
 * // 发送 Stream 消息
 * redisMqTemplate.send(new OrderStreamMessage().setOrderId(1L));
 * </pre>
 *
 * @author matrix
 */
@AllArgsConstructor
public class RedisMqTemplate {

    @Getter
    private final StringRedisTemplate redisTemplate;

    /** 拦截器数组 */
    @Getter
    private final List<RedisMessageInterceptor> interceptors = new ArrayList<>();

    /**
     * 发送 Redis Pub/Sub 消息（广播模式）。
     *
     * @param message 消息
     */
    public <T extends AbstractRedisChannelMessage> void send(T message) {
        try {
            sendMessageBefore(message);
            redisTemplate.convertAndSend(message.getChannel(), JsonUtils.toJsonString(message));
        } finally {
            sendMessageAfter(message);
        }
    }

    /**
     * 发送 Redis Stream 消息（集群消费模式）。
     *
     * @param message 消息
     * @return 消息记录 ID
     */
    public <T extends AbstractRedisStreamMessage> RecordId send(T message) {
        try {
            sendMessageBefore(message);
            return redisTemplate.opsForStream().add(StreamRecords.newRecord()
                    .ofObject(JsonUtils.toJsonString(message))
                    .withStreamKey(message.getStreamKey()));
        } finally {
            sendMessageAfter(message);
        }
    }

    private void sendMessageBefore(AbstractRedisMessage message) {
        interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
    }

    private void sendMessageAfter(AbstractRedisMessage message) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendMessageAfter(message);
        }
    }
}
