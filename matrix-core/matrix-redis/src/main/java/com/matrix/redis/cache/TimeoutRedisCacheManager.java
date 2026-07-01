package com.matrix.redis.cache;

import cn.hutool.core.util.StrUtil;
import java.time.Duration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 支持自定义超时时间的 Redis 缓存管理器
 * <p>
 * 在 cacheName 中使用 # 后缀指定过期时间：
 * <pre>
 * &#64;Cacheable(cacheNames = "user#5m")  → TTL = 5 分钟
 * &#64;Cacheable(cacheNames = "order#2h")  → TTL = 2 小时
 * &#64;Cacheable(cacheNames = "dict#30s")  → TTL = 30 秒
 * &#64;Cacheable(cacheNames = "config#1d")  → TTL = 1 天
 * </pre>
 * </p>
 *
 * @author matrix
 */
public class TimeoutRedisCacheManager extends RedisCacheManager {

    public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        // 解析 cacheName 中的 TTL 后缀：name#5m
        if (StrUtil.isBlank(name) || !name.contains("#")) {
            return super.createRedisCache(name, cacheConfig);
        }

        String[] parts = name.split("#", 2);
        String realName = parts[0];
        String ttlStr = parts[1];

        Duration ttl = parseDuration(ttlStr);
        if (ttl != null) {
            cacheConfig = cacheConfig.entryTtl(ttl);
        }

        return super.createRedisCache(realName, cacheConfig);
    }

    /**
     * 解析时间格式：30s, 5m, 2h, 1d
     */
    private Duration parseDuration(String ttlStr) {
        if (StrUtil.isBlank(ttlStr) || ttlStr.length() < 2) {
            return null;
        }
        try {
            String unit = ttlStr.substring(ttlStr.length() - 1).toLowerCase();
            long value = Long.parseLong(ttlStr.substring(0, ttlStr.length() - 1));
            return switch (unit) {
                case "s" -> Duration.ofSeconds(value);
                case "m" -> Duration.ofMinutes(value);
                case "h" -> Duration.ofHours(value);
                case "d" -> Duration.ofDays(value);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
