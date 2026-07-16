package com.matrix.feign.fallback;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import feign.FeignException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * fallback 代理处理
 * <p>使用 JDK 动态代理实现，完全兼容 JDK 21 模块强封装设计。</p>
 *
 * @param <T>
 */
@Slf4j
@AllArgsConstructor
public class DefaultFeignFallback<T> implements InvocationHandler {

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;

    @Nullable
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理 Object 的通用方法 (hashCode/equals/toString)
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        String errorMessage = cause.getMessage();
        log.error(
                "DefaultFeignFallback:[{}.{}] serviceId:[{}] message:[{}]",
                targetType.getName(),
                method.getName(),
                targetName,
                errorMessage);
        Class<?> returnType = method.getReturnType();
        // 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
        if (R.class != returnType) {
            return null;
        }
        // 非 FeignException
        if (!(cause instanceof FeignException exception)) {
            return R.fail(SystemErrorTypeEnum.FEIGN_INVOKE_ERROR, errorMessage);
        }
        final String content = exception.contentUTF8();
        // 如果返回的数据为空
        if (ObjectUtils.isEmpty(content)) {
            return R.fail(SystemErrorTypeEnum.FEIGN_INVOKE_ERROR, errorMessage);
        }
        return R.fail(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultFeignFallback<?> that = (DefaultFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }
}
