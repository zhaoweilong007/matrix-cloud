package com.matrix.mq.redis.channel;

import cn.hutool.core.util.TypeUtil;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.mq.redis.core.RedisMqTemplate;
import com.matrix.mq.redis.message.AbstractRedisMessage;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Type;

/**
 * Redis Pub/Sub 监听器抽象类，用于实现广播消费。
 *
 * @param <T> 消息类型（必须指定泛型参数）
 */
public abstract class AbstractRedisChannelMessageListener<T extends AbstractRedisChannelMessage>
        implements MessageListener {

    /** 消息类型（从泛型参数解析） */
    private final Class<T> messageType;
    /** Redis Channel 名称 */
    private final String channel;
    /** RedisMQTemplate */
    @Setter
    private RedisMqTemplate redisMqTemplate;

    @SneakyThrows
    protected AbstractRedisChannelMessageListener() {
        this.messageType = getMessageClass();
        this.channel = messageType.getDeclaredConstructor().newInstance().getChannel();
    }

    /** 获得订阅的 Redis Channel 名称 */
    public final String getChannel() {
        return channel;
    }

    @Override
    public final void onMessage(Message message, byte[] bytes) {
        T messageObj = JsonUtils.parseObject(message.getBody(), messageType);
        this.onMessage(messageObj);
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
