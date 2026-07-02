package com.matrix.mq.redis.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类。
 * 包含消息头（headers），子类可通过 {@link #addHeader(String, String)} 传递元数据。
 *
 */
@Data
public abstract class AbstractRedisMessage {

    /** 消息头，用于传递租户ID、用户ID等元数据 */
    private Map<String, String> headers = new HashMap<>();

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
