package com.matrix.websocket.core.util;

import com.matrix.common.model.login.LoginUser;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 工具类，用于在 Session Attributes 中存取 LoginUser。
 *
 */
public class WebSocketFrameworkUtils {

    /**
     * Session 属性中存储 LoginUser 的键名
     */
    private static final String LOGIN_USER_ATTRIBUTE = "LOGIN_USER";

    /**
     * 将 LoginUser 写入 Session Attributes。
     *
     * @param loginUser  登录用户
     * @param attributes Session Attributes
     */
    public static void setLoginUser(LoginUser loginUser, Map<String, Object> attributes) {
        attributes.put(LOGIN_USER_ATTRIBUTE, loginUser);
    }

    /**
     * 从 Session Attributes 中读取 LoginUser。
     *
     * @param session WebSocket 会话
     * @return 登录用户，不存在返回 null
     */
    public static LoginUser getLoginUser(WebSocketSession session) {
        return (LoginUser) session.getAttributes().get(LOGIN_USER_ATTRIBUTE);
    }

    /**
     * 从 Session 中获取 userId。
     *
     * @param session WebSocket 会话
     * @return 用户 ID，不存在返回 null
     */
    public static Long getLoginUserId(WebSocketSession session) {
        LoginUser loginUser = getLoginUser(session);
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * 从 Session 中获取 tenantId。
     *
     * @param session WebSocket 会话
     * @return 租户 ID，不存在返回 null
     */
    public static Long getTenantId(WebSocketSession session) {
        LoginUser loginUser = getLoginUser(session);
        return loginUser != null ? loginUser.getTenantId() : null;
    }
}
