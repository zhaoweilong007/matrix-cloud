package com.matrix.auth.strategy;

import com.matrix.common.model.login.LoginUser;

/**
 * 认证策略接口，统一调度多种登录方式。
 *
 * <p>每种登录方式（密码、短信、邮箱、社交登录等）实现该接口，
 * 通过 {@link AuthStrategyContext} 按 {@code grantType} 分发。</p>
 */
public interface IAuthStrategy {

    /** Bean 名称后缀，配合 grantType 拼接为 Spring Bean 名称 */
    String BASE_NAME = "AuthStrategy";

    /**
     * 执行认证，返回登录用户信息。
     *
     * @param body 登录请求体（JSON 字符串）
     * @return 登录用户信息
     */
    LoginUser authenticate(String body);
}
