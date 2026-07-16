package com.matrix.mq.redis.stream;

import cn.hutool.core.util.TypeUtil;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.mq.redis.core.RedisMqTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.lang.reflect.Type;

/**
 * Redis Stream 监听器抽象类，用于实现集群消费（Consumer Group 模式）。
 *
 * @param <T> 消息类型（必须指定泛型参数）
 */
@Slf4j
public abstract class AbstractRedisStreamMessageListener<T extends AbstractRedisStreamMessage>
        implements StreamListener<String, ObjectRecord<String, String>> {

    /** 消息类型（从泛型参数解析） */
    private final Class<T> messageType;
    /** Redis Stream Key */
    @Getter
    private final String streamKey;
    /** Redis 消费者分组，默认使用 spring.application.name */
    @Value("${spring.application.name}")
    @Getter
    private String group;
    /** RedisMQTemplate */
    @Setter
    private RedisMqTemplate redisMqTemplate;

    @SneakyThrows
    protected AbstractRedisStreamMessageListener() {
        this.messageType = getMessageClass();
        this.streamKey = messageType.getDeclaredConstructor().newInstance().getStreamKey();
    }

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        try {
            T messageObj = JsonUtils.parseObject(message.getValue(), messageType);
            this.onMessage(messageObj);
            redisMqTemplate.getRedisTemplate().opsForStream().acknowledge(group, message);
        } catch (Exception e) {
            log.error("Redis Stream 消息消费失败 streamKey=[{}] messageId=[{}]",
                    streamKey, message.getId(), e);
            // 不 ack，消息进入 PEL，由监控任务记录并由业务恢复策略处理。
        }
    }

    /**
     * 处理消息，子类实现具体业务逻辑。
     *
     * @param message 消息对象
     */
    public abstract void onMessage(T message);

    @SuppressWarnings("unchecked")
    private Class<T> getMessageClass() {
        Type type = TypeUtil.getTypeArgument(getClass(), 0);
        if (type == null) {
            throw new IllegalStateException(String.format("类型(%s) 需要设置消息类型", getClass().getName()));
        }
        return (Class<T>) type;
    }
}
