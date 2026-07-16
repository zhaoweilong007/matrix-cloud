package com.matrix.feign.fallback;

import feign.Target;
import lombok.AllArgsConstructor;
import java.lang.reflect.Proxy;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 默认fallback，减少必要的编写fallback类
 * <p>基于 JDK 原生动态代理构建代理实例。</p>
 *
 * @param <T>
 */
@AllArgsConstructor
public class DefaultFallbackFactory<T> implements FallbackFactory<T> {
    private final Target<T> target;

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();

        // 使用 JVM 原生 JDK 动态代理，无 Jigsaw 强封装访问警告，性能更高
        return (T) Proxy.newProxyInstance(
                targetType.getClassLoader(),
                new Class<?>[]{targetType},
                new DefaultFeignFallback<>(targetType, targetName, cause)
        );
    }
}
