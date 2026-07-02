package com.matrix.social.core;

import java.util.concurrent.TimeUnit;
import me.zhyd.oauth.cache.AuthStateCache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * JustAuth State 缓存 Redis 实现。
 *
 * <p>解决集群部署时 State 不一致问题，默认缓存 3 分钟。</p>
 *
 */
public class AuthRedisStateCache implements AuthStateCache {

    /**
     * State 缓存键前缀
     */
    private static final String CACHE_PREFIX = "social:state:";
    /**
     * 默认缓存超时时间，单位秒
     */
    private static final long TIMEOUT_SECONDS = 180;

    /**
     * Redisson 客户端
     */
    private final RedissonClient redissonClient;

    public AuthRedisStateCache(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void cache(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(CACHE_PREFIX + key);
        bucket.set(value, TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void cache(String key, String value, long timeout) {
        RBucket<String> bucket = redissonClient.getBucket(CACHE_PREFIX + key);
        bucket.set(value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(CACHE_PREFIX + key);
        return bucket.get();
    }

    @Override
    public boolean containsKey(String key) {
        RBucket<String> bucket = redissonClient.getBucket(CACHE_PREFIX + key);
        return bucket.isExists();
    }
}
