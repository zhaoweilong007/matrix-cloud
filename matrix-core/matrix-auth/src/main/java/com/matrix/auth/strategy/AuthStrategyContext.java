package com.matrix.auth.strategy;

import com.matrix.common.model.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 认证策略上下文，根据 {@code grantType} 分发到对应的 {@link IAuthStrategy} 实现。
 *
 * <p>Spring Bean 命名约定：{@code grantType + "AuthStrategy"}。
 * 例如 {@code grantType="password"} → Bean 名称为 {@code passwordAuthStrategy}。</p>
 */
@Component
@RequiredArgsConstructor
public class AuthStrategyContext {

    private final ApplicationContext applicationContext;

    /**
     * 根据授权类型获取对应的认证策略。
     *
     * @param grantType 授权类型（password/sms/email/social/xcx 等）
     * @return 对应的认证策略实现
     * @throws IllegalStateException 如果未找到对应策略
     */
    public IAuthStrategy getStrategy(String grantType) {
        String beanName = grantType + IAuthStrategy.BASE_NAME;
        try {
            return applicationContext.getBean(beanName, IAuthStrategy.class);
        } catch (Exception e) {
            throw new IllegalStateException("不支持的登录方式: " + grantType, e);
        }
    }

    /**
     * 根据授权类型执行认证。
     *
     * @param grantType 授权类型
     * @param body      登录请求体（JSON 字符串）
     * @return 登录用户信息
     */
    public LoginUser authenticate(String grantType, String body) {
        IAuthStrategy strategy = getStrategy(grantType);
        return strategy.authenticate(body);
    }
}
