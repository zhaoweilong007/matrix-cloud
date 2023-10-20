package com.matrix.feign.fallback;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * fallback 代理处理
 *
 * @param <T>
 */
@Slf4j
@AllArgsConstructor
public class DefaultFeignFallback<T> implements MethodInterceptor {

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;

    @Nullable
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        log.error("DefaultFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
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
