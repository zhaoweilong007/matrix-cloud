package com.matrix.websocket.core.sender.local;

import com.matrix.websocket.core.sender.AbstractWebSocketMessageSender;
import com.matrix.websocket.core.session.WebSocketSessionManager;

/**
 * 本地 WebSocket 消息发送器。
 *
 * <p>仅支持单节点部署，Session 信息存储在本地内存中。</p>
 *
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public void send(Long userId, String messageType, String messageContent) {
        sendToUser(userId, messageType, messageContent);
    }

    @Override
    public void sendBySessionId(String sessionId, String messageType, String messageContent) {
        sendToSession(sessionId, messageType, messageContent);
    }

    @Override
    public void sendToAll(String messageType, String messageContent) {
        sendToAllSessions(messageType, messageContent);
    }
}
