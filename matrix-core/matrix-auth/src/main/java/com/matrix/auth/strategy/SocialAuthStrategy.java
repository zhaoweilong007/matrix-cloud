package com.matrix.auth.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.model.login.LoginUser;
import com.matrix.social.core.SocialAuthService;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Component;

/**
 * 社交登录认证策略。
 *
 * <p>从请求体中解析 {@code platform} 和授权回调参数，
 * 委托 {@link SocialAuthService} 完成第三方登录，
 * 将 {@link AuthUser} 映射为 {@link LoginUser}。</p>
 */
@Component("social" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SocialAuthStrategy implements IAuthStrategy {

    private final SocialAuthService socialAuthService;

    /**
     * 社交登录认证。
     */
    @Override
    public LoginUser authenticate(String body) {
        JSONObject json = JSON.parseObject(body);
        String platform = json.getString("platform");
        String authCode = json.getString("code");
        String authState = json.getString("state");

        if (platform == null || authCode == null) {
            throw new ServiceException(BusinessErrorTypeEnum.AUTH_MINI_USER_INFO_ERROR);
        }

        // 构建 JustAuth 回调并委托社交登录
        AuthCallback callback = new AuthCallback();
        callback.setCode(authCode);
        callback.setState(authState);
        AuthUser authUser = socialAuthService.login(platform, callback);

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(authUser.getNickname());
        loginUser.setMobile(authUser.getUsername());
        return loginUser;
    }
}
