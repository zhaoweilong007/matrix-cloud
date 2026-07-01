package com.matrix.social.core;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import me.zhyd.oauth.log.Log;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;

/**
 * 第三方登录统一服务。
 *
 * <p>提供 authorize（跳转授权页）和 login（处理回调）两个核心能力。</p>
 *
 * @author matrix
 */
public class SocialAuthService {

    private final SocialAuthFactory authFactory;

    public SocialAuthService(SocialAuthFactory authFactory) {
        this.authFactory = authFactory;
    }

    /**
     * 获取第三方平台授权 URL，用户浏览器跳转后进入授权页。
     *
     * @param platform 平台标识（gitee/github/wechat_open 等）
     * @param state    防 CSRF 参数（可选）
     * @return 授权 URL
     */
    public String authorize(String platform, String state) {
        AuthRequest authRequest = authFactory.getAuthRequest(platform);
        return authRequest.authorize(state);
    }

    /**
     * 获取授权 URL 并直接 302 重定向。
     *
     * @param platform 平台标识
     * @param response HTTP 响应
     * @throws IOException 重定向失败
     */
    public void redirect(String platform, HttpServletResponse response) throws IOException {
        String url = authorize(platform, null);
        response.sendRedirect(url);
    }

    /**
     * 处理第三方回调，获取用户信息。
     *
     * @param platform 平台标识
     * @param callback 回调参数（code、state 等）
     * @return 第三方用户信息
     */
    public AuthUser login(String platform, AuthCallback callback) {
        AuthRequest authRequest = authFactory.getAuthRequest(platform);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (!response.ok()) {
            throw new RuntimeException("第三方登录失败: " + response.getMsg());
        }
        return response.getData();
    }
}
