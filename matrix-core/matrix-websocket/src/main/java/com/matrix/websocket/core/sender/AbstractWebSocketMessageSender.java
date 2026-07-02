package com.matrix.websocket.core.sender;

import com.matrix.common.util.json.JsonUtils;
import com.matrix.websocket.core.message.JsonWebSocketMessage;
import com.matrix.websocket.core.session.WebSocketSessionManager;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 消息发送器骨架实现。
 *
 * <p>封装了本地 Session 查找和消息发送的核心逻辑。
 * 所有 MQ 版本的 Consumer 最终都回调到
 * {@link #doSend} 方法，实现跨节点消息投递。</p>
 *
 */
public abstract class AbstractWebSocketMessageSender implements WebSocketMessageSender {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * WebSocket Session 管理器，用于查找目标 Session
     */
    protected final WebSocketSessionManager sessionManager;

    protected AbstractWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 向指定用户的所有连接发送消息（本地投递）。
     */
    protected void sendToUser(Long userId, String messageType, String messageContent) {
        List<WebSocketSession> sessions = sessionManager.getSessionListByUserId(userId);
        doSend(sessions, messageType, messageContent);
    }

    /**
     * 向指定 Session 发送消息。
     */
    protected void sendToSession(String sessionId, String messageType, String messageContent) {
        WebSocketSession session = sessionManager.getSession(sessionId);
        if (session != null) {
            doSend(List.of(session), messageType, messageContent);
        }
    }

    /**
     * 向所有在线用户广播消息。
     */
    protected void sendToAllSessions(String messageType, String messageContent) {
        List<WebSocketSession> allSessions = sessionManager.getAllSessions();
        doSend(allSessions, messageType, messageContent);
    }

    /**
     * 实际执行消息序列化和投递。
     *
     * @param sessions       目标 Session 列表
     * @param messageType    消息类型
     * @param messageContent 消息内容（JSON 字符串）
     */
    protected void doSend(List<WebSocketSession> sessions, String messageType, String messageContent) {
        if (sessions == null || sessions.isEmpty()) {
            return;
        }
        String payload = JsonUtils.toJsonString(new JsonWebSocketMessage(messageType, messageContent));
        TextMessage textMessage = new TextMessage(payload);
        for (WebSocketSession session : sessions) {
            if (session == null || !session.isOpen()) {
                continue;
            }
            try {
                synchronized (session) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                log.debug("Failed to send WebSocket message to session {}: {}", session.getId(), e.getMessage());
            }
        }
    }
}
