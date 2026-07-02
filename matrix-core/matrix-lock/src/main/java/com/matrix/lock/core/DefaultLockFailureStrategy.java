package com.matrix.lock.core;

import com.baomidou.lock.LockFailureStrategy;
import com.matrix.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 自定义锁获取失败策略。
 *
 * <p>当业务方法获取分布式锁超时或失败时，抛出
 * {@link ServiceException}(503) 与友好提示信息，
 * 避免前端收到 500 内部错误。</p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Slf4j
public class DefaultLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        log.warn("获取分布式锁失败, key=[{}], method=[{}#{}]",
                key, method.getDeclaringClass().getSimpleName(), method.getName());
        throw new ServiceException(503, "处理中，请稍后再试");
    }
}
