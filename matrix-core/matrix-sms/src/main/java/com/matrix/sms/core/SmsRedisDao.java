package com.matrix.sms.core;

import com.matrix.redis.utils.RedisUtils;
import org.dromara.sms4j.api.dao.SmsDao;

import java.time.Duration;

/**
 * Redis 缓存的 SMS Dao 实现。
 *
 * <p>实现 sms4j 的 {@link SmsDao} 接口，基于 Redis 提供短信重试和拦截
 * 所需的缓存能力（发送间隔限流、重试计数等）。</p>
 *
 */
public class SmsRedisDao implements SmsDao {

    private static final String SMS_KEY_PREFIX = "sms:";

    /**
     * 设置带过期时间的缓存。
     *
     * @param key       缓存键
     * @param value     缓存值
     * @param cacheTime 过期时间（秒）
     */
    @Override
    public void set(String key, Object value, long cacheTime) {
        RedisUtils.setCacheObject(SMS_KEY_PREFIX + key, value, Duration.ofSeconds(cacheTime));
    }

    /**
     * 设置永不过期的缓存。
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    @Override
    public void set(String key, Object value) {
        RedisUtils.setCacheObject(SMS_KEY_PREFIX + key, value, true);
    }

    /**
     * 获取缓存值。
     *
     * @param key 缓存键
     * @return 缓存值，不存在返回 null
     */
    @Override
    public Object get(String key) {
        return RedisUtils.getCacheObject(SMS_KEY_PREFIX + key);
    }

    /**
     * 删除缓存。
     *
     * @param key 缓存键
     * @return 是否删除成功
     */
    @Override
    public Object remove(String key) {
        return RedisUtils.deleteObject(SMS_KEY_PREFIX + key);
    }

    /**
     * 清空所有短信相关缓存。
     */
    @Override
    public void clean() {
        RedisUtils.deleteKeys(SMS_KEY_PREFIX + "*");
    }
}
