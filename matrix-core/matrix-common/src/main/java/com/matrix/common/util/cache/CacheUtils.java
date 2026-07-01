package com.matrix.common.util.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存工具（基于 Guava LoadingCache）
 * <p>
 * 支持异步刷新模式：缓存过期后后台线程自动刷新，不阻塞请求线程
 * </p>
 *
 * @author matrix
 */
public class CacheUtils {

    /**
     * 构建异步刷新的本地缓存
     * <p>
     * 当缓存过期后，返回旧值同时异步刷新缓存，避免缓存击穿阻塞请求
     * </p>
     *
     * @param refreshDuration 刷新间隔（过期后旧值仍可用此时间）
     * @param loader          缓存加载器
     * @param <K>             Key 类型
     * @param <V>             Value 类型
     * @return LoadingCache 实例
     */
    public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration refreshDuration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                // 只阻塞当前数据加载线程，其他线程返回旧值
                .refreshAfterWrite(refreshDuration)
                // 通过 asyncReloading 实现全异步加载
                .build(CacheLoader.asyncReloading(
                        loader, Executors.newVirtualThreadPerTaskExecutor()));
    }

    /**
     * 构建简单本地缓存（固定过期时间）
     *
     * @param expireDuration 过期时间
     * @param loader         缓存加载器
     * @param <K>            Key 类型
     * @param <V>            Value 类型
     * @return LoadingCache 实例
     */
    public static <K, V> LoadingCache<K, V> buildCache(Duration expireDuration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expireDuration.toMillis(), TimeUnit.MILLISECONDS)
                .build(loader);
    }

    /**
     * 构建最大容量限制的本地缓存
     *
     * @param maxSize        最大容量
     * @param expireDuration 过期时间
     * @param loader         缓存加载器
     * @param <K>            Key 类型
     * @param <V>            Value 类型
     * @return LoadingCache 实例
     */
    public static <K, V> LoadingCache<K, V> buildCache(long maxSize, Duration expireDuration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireDuration.toMillis(), TimeUnit.MILLISECONDS)
                .build(loader);
    }
}
