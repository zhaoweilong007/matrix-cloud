package com.matrix.websocket.core.session;

import com.matrix.common.model.login.LoginUser;
import com.matrix.websocket.core.util.WebSocketFrameworkUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket Session 管理器默认实现。
 *
 * <p>使用纯内存 ConcurrentHashMap 存储，双层索引：</p>
 * <ul>
 *   <li>idSessions: sessionId → Session（O(1) 精确查找）</li>
 *   <li>userSessions: userId → Session 列表（支持一个用户多端连接）</li>
 * </ul>
 *
 */
public class WebSocketSessionManagerImpl implements WebSocketSessionManager {

    /** sessionId → Session */
    private final Map<String, WebSocketSession> idSessions = new ConcurrentHashMap<>();

    /** userId → Session 列表（一个用户可多端登录） */
    private final Map<Long, CopyOnWriteArrayList<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    @Override
    public void addSession(WebSocketSession session) {
        idSessions.put(session.getId(), session);
        LoginUser loginUser = WebSocketFrameworkUtils.getLoginUser(session);
        if (loginUser != null && loginUser.getUserId() != null) {
            userSessions.computeIfAbsent(loginUser.getUserId(), k -> new CopyOnWriteArrayList<>())
                    .add(session);
        }
    }

    @Override
    public void removeSession(WebSocketSession session) {
        idSessions.remove(session.getId());
        LoginUser loginUser = WebSocketFrameworkUtils.getLoginUser(session);
        if (loginUser != null && loginUser.getUserId() != null) {
            List<WebSocketSession> sessions = userSessions.get(loginUser.getUserId());
            if (sessions != null) {
                sessions.removeIf(s -> s.getId().equals(session.getId()));
                if (sessions.isEmpty()) {
                    userSessions.remove(loginUser.getUserId());
                }
            }
        }
    }

    @Override
    public WebSocketSession getSession(String sessionId) {
        return idSessions.get(sessionId);
    }

    @Override
    public List<WebSocketSession> getSessionListByUserId(Long userId) {
        List<WebSocketSession> sessions = userSessions.get(userId);
        return sessions != null ? new ArrayList<>(sessions) : Collections.emptyList();
    }

    @Override
    public List<WebSocketSession> getAllSessions() {
        List<WebSocketSession> result = new ArrayList<>();
        for (CopyOnWriteArrayList<WebSocketSession> sessions : userSessions.values()) {
            result.addAll(sessions);
        }
        return result;
    }
}
