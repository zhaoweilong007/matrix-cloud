package com.matrix.websocket.core.sender.redis;

import com.matrix.common.util.json.JsonUtils;
import com.matrix.websocket.core.sender.AbstractWebSocketMessageSender;
import com.matrix.websocket.core.session.WebSocketSessionManager;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis WebSocket 消息消费者。
 *
 * <p>订阅 Redis Channel 接收其他节点广播的消息，
 * 在本地 Session 中查找目标并投递。</p>
 *
 */
public class RedisWebSocketMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(RedisWebSocketMessageConsumer.class);

    /**
     * 本地消息发送器，用于接收 Redis 广播后在当前节点投递消息
     */
    private final AbstractWebSocketMessageSender localSender;

    public RedisWebSocketMessageConsumer(RedissonClient redissonClient, WebSocketSessionManager sessionManager) {
        this.localSender = new AbstractWebSocketMessageSender(sessionManager) {
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
        };

        // 订阅 Redis Topic
        RTopic topic = redissonClient.getTopic(RedisWebSocketMessageSender.TOPIC);
        topic.addListener(String.class, (channel, msg) -> {
            try {
                RedisWebSocketMessage wsMsg = JsonUtils.parseObject(msg, RedisWebSocketMessage.class);
                if (wsMsg != null) {
                    handleMessage(wsMsg);
                }
            } catch (Exception e) {
                log.error("Failed to handle Redis WebSocket message", e);
            }
        });
        log.info("Redis WebSocket consumer subscribed to topic: {}", RedisWebSocketMessageSender.TOPIC);
    }

    /**
     * 处理收到的 Redis 广播消息，根据消息中的目标信息进行本地投递
     *
     * @param msg Redis 广播消息
     */
    private void handleMessage(RedisWebSocketMessage msg) {
        if (msg.getSessionId() != null) {
            localSender.sendBySessionId(msg.getSessionId(), msg.getMessageType(), msg.getMessageContent());
        } else if (msg.getUserId() != null) {
            localSender.send(msg.getUserId(), msg.getMessageType(), msg.getMessageContent());
        } else {
            localSender.sendToAll(msg.getMessageType(), msg.getMessageContent());
        }
    }
}
