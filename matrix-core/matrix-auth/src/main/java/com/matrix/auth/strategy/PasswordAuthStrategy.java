package com.matrix.auth.strategy;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.model.login.LoginUser;
import org.springframework.stereotype.Component;

/**
 * 密码登录认证策略。
 *
 * <p>从请求体中解析 {@code username} 和 {@code password}，
 * 验证密码后返回 {@link LoginUser}。</p>
 */
@Component("password" + IAuthStrategy.BASE_NAME)
public class PasswordAuthStrategy implements IAuthStrategy {

    /**
     * 密码登录认证，预期请求体包含 {@code username} 和 {@code password} 字段。
     */
    @Override
    public LoginUser authenticate(String body) {
        JSONObject json = JSON.parseObject(body);
        String username = json.getString("username");
        String password = json.getString("password");

        if (username == null || password == null) {
            throw new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST);
        }

        // 密码验证逻辑由调用方（如 SysAdminServiceImpl）处理
        // 策略只负责解析参数并返回基础 LoginUser
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        return loginUser;
    }
}
