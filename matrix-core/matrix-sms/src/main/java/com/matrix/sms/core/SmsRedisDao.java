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
 * @author matrix
 */
public class SmsRedisDao implements SmsDao {

    private static final String SMS_KEY_PREFIX = "sms:";

    @Override
    public void set(String key, Object value, long cacheTime) {
        RedisUtils.setCacheObject(SMS_KEY_PREFIX + key, value, Duration.ofSeconds(cacheTime));
    }

    @Override
    public void set(String key, Object value) {
        RedisUtils.setCacheObject(SMS_KEY_PREFIX + key, value, true);
    }

    @Override
    public Object get(String key) {
        return RedisUtils.getCacheObject(SMS_KEY_PREFIX + key);
    }

    @Override
    public Object remove(String key) {
        return RedisUtils.deleteObject(SMS_KEY_PREFIX + key);
    }

    @Override
    public void clean() {
        RedisUtils.deleteKeys(SMS_KEY_PREFIX + "*");
    }
}
