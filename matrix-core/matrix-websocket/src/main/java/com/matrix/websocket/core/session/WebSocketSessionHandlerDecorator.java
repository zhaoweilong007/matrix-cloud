package com.matrix.websocket.core.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocket Handler 装饰器，负责 Session 生命周期的自动管理。
 *
 * <p>建立连接时将 Session 加入 {@link WebSocketSessionManager}，
 * 关闭连接时自动移除。</p>
 *
 */
public class WebSocketSessionHandlerDecorator extends WebSocketHandlerDecorator {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionHandlerDecorator.class);

    /**
     * Session 管理器，用于管理 WebSocket 会话的生命周期
     */
    private final WebSocketSessionManager sessionManager;

    public WebSocketSessionHandlerDecorator(WebSocketHandler delegate, WebSocketSessionManager sessionManager) {
        super(delegate);
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionManager.addSession(session);
        log.debug("WebSocket session established: id={}, uri={}", session.getId(), session.getUri());
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionManager.removeSession(session);
        log.debug("WebSocket session closed: id={}, status={}", session.getId(), closeStatus);
        super.afterConnectionClosed(session, closeStatus);
    }
}
