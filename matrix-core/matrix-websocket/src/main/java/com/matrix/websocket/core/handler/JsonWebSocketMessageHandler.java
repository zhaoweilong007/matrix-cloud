package com.matrix.websocket.core.handler;

import com.matrix.common.util.json.JsonUtils;
import com.matrix.websocket.core.listener.WebSocketMessageListener;
import com.matrix.websocket.core.message.JsonWebSocketMessage;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * JSON WebSocket 消息处理器。
 *
 * <p>负责接收前端发来的消息，根据消息类型分发给对应的 {@link WebSocketMessageListener}。
 * 自动处理 "ping" 心跳消息。</p>
 *
 * @author matrix
 */
public class JsonWebSocketMessageHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(JsonWebSocketMessageHandler.class);

    private final Map<String, WebSocketMessageListener<?>> listeners = new HashMap<>();

    public JsonWebSocketMessageHandler(List<WebSocketMessageListener<?>> listenerList) {
        for (WebSocketMessageListener<?> listener : listenerList) {
            listeners.put(listener.getType(), listener);
        }
        log.info("Registered {} WebSocket message listeners: {}", listeners.size(), listeners.keySet());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if (payload == null || payload.isBlank()) {
            return;
        }
        // 心跳处理
        if ("ping".equals(payload.trim())) {
            session.sendMessage(new TextMessage("pong"));
            return;
        }
        try {
            JsonWebSocketMessage wsMessage = JsonUtils.parseObject(payload, JsonWebSocketMessage.class);
            if (wsMessage == null || wsMessage.getType() == null) {
                log.warn("Invalid WebSocket message format: {}", payload);
                return;
            }
            dispatchMessage(session, wsMessage);
        } catch (Exception e) {
            log.error("Failed to handle WebSocket message: {}", payload, e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void dispatchMessage(WebSocketSession session, JsonWebSocketMessage wsMessage) throws Exception {
        WebSocketMessageListener listener = listeners.get(wsMessage.getType());
        if (listener == null) {
            log.warn("No listener for WebSocket message type: {}", wsMessage.getType());
            return;
        }
        // 通过反射获取泛型参数类型，反序列化 content
        Class<?> messageClass = getMessageClass(listener);
        Object messageObj;
        if (messageClass != null && wsMessage.getContent() != null) {
            messageObj = JsonUtils.parseObject(wsMessage.getContent(), messageClass);
        } else {
            messageObj = wsMessage.getContent();
        }
        listener.onMessage(session, messageObj);
    }

    /**
     * 从监听器的泛型参数中提取消息类型。
     */
    private Class<?> getMessageClass(WebSocketMessageListener<?> listener) {
        // 遍历接口以找到 WebSocketMessageListener
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType pt) {
                if (pt.getRawType() == WebSocketMessageListener.class) {
                    Type[] args = pt.getActualTypeArguments();
                    if (args.length > 0 && args[0] instanceof Class<?> c) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.debug("WebSocket connection closed: sessionId={}, status={}", session.getId(), status);
    }
}
