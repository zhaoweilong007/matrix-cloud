package com.matrix.mq.redis.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.mq.redis.message.AbstractRedisMessage;

/**
 * Redis Channel（Pub/Sub）消息抽象类。
 * <p>默认使用类名作为 Channel 名称，子类可直接继承使用。</p>
 *
 * @author matrix
 */
public abstract class AbstractRedisChannelMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Channel 名称，默认使用类名。
     */
    @JsonIgnore
    public String getChannel() {
        return getClass().getSimpleName();
    }
}
