package com.matrix.auth.core;

import com.matrix.auto.properties.UserPasswordProperties;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 密码锁定服务。
 *
 * <p>基于 Redis 计数器实现登录失败锁定策略：
 * 密码错误次数达到 {@link UserPasswordProperties#getMaxRetryCount()} 后，
 * 锁定 {@link UserPasswordProperties#getLockTime()} 分钟。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordLockoutService {

    private final UserPasswordProperties userPasswordProperties;

    /**
     * 检查账户是否被锁定。
     *
     * @param username 用户名
     * @throws ServiceException 如果账户已被锁定
     */
    public void checkLocked(String username) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        Integer errorCount = RedisUtils.getCacheObject(errorKey);
        if (errorCount != null && errorCount >= userPasswordProperties.getMaxRetryCount()) {
            log.warn("账户已被锁定, username=[{}], errorCount=[{}]", username, errorCount);
            throw new ServiceException(SystemErrorTypeEnum.OPERATE_FAIL,
                    "密码错误次数过多，账户已被锁定，请" + userPasswordProperties.getLockTime() + "分钟后重试");
        }
    }

    /**
     * 记录密码错误，达到最大次数后抛出异常。
     *
     * @param username 用户名
     * @throws ServiceException 如果达到最大重试次数
     */
    public void recordPasswordError(String username) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        Integer errorCount = RedisUtils.getCacheObject(errorKey);
        errorCount = (errorCount == null) ? 1 : errorCount + 1;

        RedisUtils.setCacheObject(errorKey, errorCount, Duration.ofMinutes(userPasswordProperties.getLockTime()));

        if (errorCount >= userPasswordProperties.getMaxRetryCount()) {
            log.warn("密码错误次数达到上限，锁定账户, username=[{}], errorCount=[{}]", username, errorCount);
            throw new ServiceException(SystemErrorTypeEnum.OPERATE_FAIL,
                    "密码错误次数过多，账户已被锁定，请" + userPasswordProperties.getLockTime() + "分钟后重试");
        }
        log.debug("密码错误, username=[{}], errorCount=[{}]", username, errorCount);
    }

    /**
     * 密码验证成功后清除错误计数。
     *
     * @param username 用户名
     */
    public void clearErrorCount(String username) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        RedisUtils.deleteObject(errorKey);
    }
}
