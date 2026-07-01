package com.matrix.mq.redis.stream;

import cn.hutool.core.util.TypeUtil;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.mq.redis.core.RedisMqTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.lang.reflect.Type;

/**
 * Redis Stream 监听器抽象类，用于实现集群消费（Consumer Group 模式）。
 *
 * @param <T> 消息类型（必须指定泛型参数）
 * @author matrix
 */
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
        T messageObj = JsonUtils.parseObject(message.getValue(), messageType);
        this.onMessage(messageObj);
        // ack 消息消费完成
        redisMqTemplate.getRedisTemplate().opsForStream().acknowledge(group, message);
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
