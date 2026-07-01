package com.matrix.social.platform;

import cn.hutool.core.lang.Dict;
import com.matrix.common.util.json.JsonUtils;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * MaxKey SSO 认证请求实现。
 *
 * @author matrix
 */
public class AuthMaxKeyRequest extends AuthDefaultRequest {

    /** MaxKey 服务器地址，从配置读取 */
    public static final String SERVER_URL = getServerUrl();

    public AuthMaxKeyRequest(AuthConfig config) {
        super(config, AuthMaxKeySource.MAXKEY);
    }

    public AuthMaxKeyRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthMaxKeySource.MAXKEY, authStateCache);
    }

    @Override
    public AuthToken getAccessToken(AuthCallback authCallback) {
        String body = doPostAuthorizationCode(authCallback.getCode());
        Dict object = JsonUtils.parseMap(body);
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        if (object.containsKey("message")) {
            throw new AuthException(object.getStr("message"));
        }
        return AuthToken.builder()
            .accessToken(object.getStr("access_token"))
            .refreshToken(object.getStr("refresh_token"))
            .idToken(object.getStr("id_token"))
            .tokenType(object.getStr("token_type"))
            .scope(object.getStr("scope"))
            .build();
    }

    @Override
    public AuthUser getUserInfo(AuthToken authToken) {
        String body = doGetUserInfo(authToken);
        Dict object = JsonUtils.parseMap(body);
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        return AuthUser.builder()
            .uuid(object.getStr("userId"))
            .username(object.getStr("username"))
            .nickname(object.getStr("displayName"))
            .avatar(object.getStr("avatar_url"))
            .blog(object.getStr("web_url"))
            .company(object.getStr("organization"))
            .location(object.getStr("location"))
            .email(object.getStr("email"))
            .remark(object.getStr("bio"))
            .token(authToken)
            .source(source.toString())
            .build();
    }

    private static String getServerUrl() {
        try {
            Class<?> clazz = Class.forName("com.matrix.common.util.spring.SpringUtils");
            return (String) clazz.getMethod("getProperty", String.class).invoke(null, "justauth.type.maxkey.server-url");
        } catch (Exception e) {
            return "";
        }
    }
}
