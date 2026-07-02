package com.matrix.redis.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

/**
 * Caffeine (L1) + Redis (L2) 二级缓存装饰器。
 *
 * <p>读取流程：Caffeine.get() → 命中返回，未命中 → Redis.get() → 回填 Caffeine。
 * 写入流程：Redis.put() → Caffeine.put() 同步更新。
 * 失效流程：Redis.evict() → Caffeine.invalidate()。</p>
 *
 */
public class CaffeineRedisCacheDecorator implements org.springframework.cache.Cache {

    private static final Logger log = LoggerFactory.getLogger(CaffeineRedisCacheDecorator.class);

    private final org.springframework.cache.Cache redisCache;
    private final Cache<Object, Object> caffeineCache;
    private final String name;

    /** 默认 L1 最大容量 */
    private static final long DEFAULT_MAX_SIZE = 1000;
    /** 默认 L1 过期时间（秒） */
    private static final long DEFAULT_TTL_SECONDS = 30;

    public CaffeineRedisCacheDecorator(String name, org.springframework.cache.Cache redisCache) {
        this(name, redisCache, DEFAULT_MAX_SIZE, DEFAULT_TTL_SECONDS);
    }

    public CaffeineRedisCacheDecorator(String name, org.springframework.cache.Cache redisCache,
            long maxSize, long ttlSeconds) {
        this.name = name;
        this.redisCache = redisCache;
        this.caffeineCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                .recordStats()
                .build();
        log.debug("Caffeine L1 cache created: name={}, maxSize={}, ttl={}s", name, maxSize, ttlSeconds);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        // L1: Caffeine
        Object value = caffeineCache.getIfPresent(key);
        if (value == NULL_HOLDER) {
            return null;
        }
        if (value != null) {
            return () -> value;
        }
        // L2: Redis
        ValueWrapper wrapper = redisCache.get(key);
        if (wrapper != null) {
            caffeineCache.put(key, wrapper.get());
            return wrapper;
        }
        return null;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, @Nullable Class<T> type) {
        Object value = caffeineCache.getIfPresent(key);
        if (value == NULL_HOLDER) {
            return null;
        }
        if (value != null && (type == null || type.isInstance(value))) {
            return (T) value;
        }
        T result = redisCache.get(key, type);
        if (result != null) {
            caffeineCache.put(key, result);
        }
        return result;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 使用 Caffeine 的原子加载能力
        try {
            return (T) caffeineCache.get(key, k -> {
                ValueWrapper wrapper = redisCache.get(k);
                if (wrapper != null) {
                    return wrapper.get();
                }
                try {
                    T loaded = valueLoader.call();
                    redisCache.put(k, loaded);
                    return loaded;
                } catch (Exception e) {
                    throw new ValueRetrievalException(k, valueLoader, e);
                }
            });
        } catch (Exception e) {
            if (e instanceof ValueRetrievalException) {
                throw (ValueRetrievalException) e;
            }
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        redisCache.put(key, value);
        if (value != null) {
            caffeineCache.put(key, value);
        } else {
            caffeineCache.put(key, NULL_HOLDER);
        }
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
        caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        redisCache.clear();
        caffeineCache.invalidateAll();
    }

    /** 空值占位符（避免缓存穿透） */
    private static final Object NULL_HOLDER = new Object();
}
