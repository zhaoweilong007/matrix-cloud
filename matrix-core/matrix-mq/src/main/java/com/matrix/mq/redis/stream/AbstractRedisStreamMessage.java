package com.matrix.mq.redis.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.mq.redis.message.AbstractRedisMessage;

/**
 * Redis Stream 消息抽象类。
 * <p>默认使用类名作为 Stream Key，子类可直接继承使用。</p>
 *
 * @author matrix
 */
public abstract class AbstractRedisStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key，默认使用类名。
     */
    @JsonIgnore
    public String getStreamKey() {
        return getClass().getSimpleName();
    }
}
