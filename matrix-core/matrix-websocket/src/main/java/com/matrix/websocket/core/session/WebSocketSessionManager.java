package com.matrix.websocket.core.session;

import java.util.List;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket Session 管理器接口。
 *
 * @author matrix
 */
public interface WebSocketSessionManager {

    /**
     * 添加 Session 到管理器中。
     *
     * @param session WebSocket 会话
     */
    void addSession(WebSocketSession session);

    /**
     * 从管理器中移除 Session。
     *
     * @param session WebSocket 会话
     */
    void removeSession(WebSocketSession session);

    /**
     * 根据 sessionId 获取 Session。
     *
     * @param sessionId 会话 ID
     * @return WebSocket 会话，不存在返回 null
     */
    WebSocketSession getSession(String sessionId);

    /**
     * 获取指定用户的所有 Session 列表。
     *
     * @param userId 用户 ID
     * @return 匹配的 Session 列表
     */
    List<WebSocketSession> getSessionListByUserId(Long userId);

    /**
     * 获取所有在线 Session 列表。
     *
     * @return 所有 Session 列表
     */
    List<WebSocketSession> getAllSessions();
}
