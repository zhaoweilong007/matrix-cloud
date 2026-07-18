package com.matrix.redis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

class TimeoutRedisCacheManagerTest {

    @Test
    void preservesDeclaredCacheNameAndAppliesTtl() {
        TestCacheManager manager = new TestCacheManager();

        RedisCache cache = manager.create("user#5m");

        assertEquals("user#5m", cache.getName());
        assertEquals(Duration.ofMinutes(5), ttl(cache));
    }

    @Test
    void fallsBackToDefaultTtlForInvalidOrNonPositiveSuffix() {
        TestCacheManager manager = new TestCacheManager();

        assertEquals(Duration.ZERO, ttl(manager.create("user#invalid")));
        assertEquals(Duration.ZERO, ttl(manager.create("user#0s")));
        assertEquals(Duration.ZERO, ttl(manager.create("user#-2m")));
    }

    private static Duration ttl(RedisCache cache) {
        return cache.getCacheConfiguration().getTtlFunction().getTimeToLive("key", null);
    }

    private static final class TestCacheManager extends TimeoutRedisCacheManager {

        private TestCacheManager() {
            super(mock(RedisCacheWriter.class), RedisCacheConfiguration.defaultCacheConfig());
        }

        private RedisCache create(String name) {
            return createRedisCache(name, RedisCacheConfiguration.defaultCacheConfig());
        }
    }
}
