package com.matrix.social.platform;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Gitea OAuth2 平台定义，提供授权、令牌、用户信息等端点 URL
 */
public enum AuthGiteaSource implements AuthSource {

    GITEA {
        @Override
        public String authorize() {
            return AuthGiteaRequest.SERVER_URL + "/login/oauth/authorize";
        }

        @Override
        public String accessToken() {
            return AuthGiteaRequest.SERVER_URL + "/login/oauth/access_token";
        }

        @Override
        public String userInfo() {
            return AuthGiteaRequest.SERVER_URL + "/login/oauth/userinfo";
        }

        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthGiteaRequest.class;
        }
    }
}
