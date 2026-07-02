package com.matrix.social.platform;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.matrix.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Gitea OAuth2 认证请求实现，支持通过配置自定义 Gitea 服务器地址
 */
@Slf4j
public class AuthGiteaRequest extends AuthDefaultRequest {

    /**
     * Gitea 服务器地址，从配置读取
     */
    public static final String SERVER_URL = getServerUrl();

    public AuthGiteaRequest(AuthConfig config) {
        super(config, AuthGiteaSource.GITEA);
    }

    public AuthGiteaRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthGiteaSource.GITEA, authStateCache);
    }

    @Override
    public AuthToken getAccessToken(AuthCallback authCallback) {
        String body = doPostAuthorizationCode(authCallback.getCode());
        Dict object = JsonUtils.parseMap(body);
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        return AuthToken.builder()
            .accessToken(object.getStr("access_token"))
            .refreshToken(object.getStr("refresh_token"))
            .tokenType(object.getStr("token_type"))
            .scope(object.getStr("scope"))
            .build();
    }

    @Override
    protected String doPostAuthorizationCode(String code) {
        HttpRequest request = HttpRequest.post(source.accessToken())
            .form("client_id", config.getClientId())
            .form("client_secret", config.getClientSecret())
            .form("grant_type", "authorization_code")
            .form("code", code)
            .form("redirect_uri", config.getRedirectUri());
        HttpResponse response = request.execute();
        return response.body();
    }

    @Override
    public AuthUser getUserInfo(AuthToken authToken) {
        String body = doGetUserInfo(authToken);
        Dict object = JsonUtils.parseMap(body);
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        return AuthUser.builder()
            .uuid(object.getStr("sub"))
            .username(object.getStr("name"))
            .nickname(object.getStr("preferred_username"))
            .avatar(object.getStr("picture"))
            .email(object.getStr("email"))
            .token(authToken)
            .source(source.toString())
            .build();
    }

    private static String getServerUrl() {
        try {
            Class<?> clazz = Class.forName("com.matrix.common.util.spring.SpringUtils");
            return (String) clazz.getMethod("getProperty", String.class).invoke(null, "justauth.type.gitea.server-url");
        } catch (Exception e) {
            return "";
        }
    }
}
