package com.matrix.redis.cache;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;

class CaffeineRedisCacheDecoratorTest {

    @Test
    void cachesNullValueFromRedisWithoutCallingRedisAgain() {
        Cache redis = mock(Cache.class);
        when(redis.get("key")).thenReturn(() -> null);
        CaffeineRedisCacheDecorator cache = new CaffeineRedisCacheDecorator("test", redis);

        assertNull(cache.get("key").get());
        assertNull(cache.get("key").get());

        verify(redis, times(1)).get("key");
    }

    @Test
    void cachesNullValueReturnedByLoader() throws Exception {
        Cache redis = mock(Cache.class);
        @SuppressWarnings("unchecked")
        Callable<Object> loader = mock(Callable.class);
        when(loader.call()).thenReturn(null);
        CaffeineRedisCacheDecorator cache = new CaffeineRedisCacheDecorator("test", redis);

        assertNull(cache.get("key", loader));
        assertNull(cache.get("key", loader));

        verify(loader, times(1)).call();
        verify(redis, times(1)).put("key", null);
    }

    @Test
    void returnsCachedNonNullValue() {
        Cache redis = mock(Cache.class);
        Object value = new Object();
        when(redis.get("key")).thenReturn(() -> value);
        CaffeineRedisCacheDecorator cache = new CaffeineRedisCacheDecorator("test", redis);

        assertSame(value, cache.get("key").get());
        assertSame(value, cache.get("key").get());

        verify(redis, times(1)).get("key");
    }
}
