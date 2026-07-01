package com.matrix.websocket.core.sender.redis;

import com.matrix.common.util.json.JsonUtils;
import com.matrix.websocket.core.sender.AbstractWebSocketMessageSender;
import com.matrix.websocket.core.session.WebSocketSessionManager;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * Redis Pub/Sub 广播方式发送 WebSocket 消息。
 *
 * <p>通过 Redisson RTopic 将消息广播给所有节点，
 * 各节点收到后根据 userId 在本地 Session 管理器中查找并投递。</p>
 *
 * @author matrix
 */
public class RedisWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public static final String TOPIC = "websocket:messages";

    private final RTopic redisTopic;

    private final RedissonClient redissonClient;

    public RedisWebSocketMessageSender(WebSocketSessionManager sessionManager, RedissonClient redissonClient) {
        super(sessionManager);
        this.redissonClient = redissonClient;
        this.redisTopic = redissonClient.getTopic(TOPIC);
    }

    @Override
    public void send(Long userId, String messageType, String messageContent) {
        // 广播到所有节点
        RedisWebSocketMessage msg = new RedisWebSocketMessage(userId, null, messageType, messageContent);
        redisTopic.publish(JsonUtils.toJsonString(msg));
    }

    @Override
    public void sendBySessionId(String sessionId, String messageType, String messageContent) {
        // sessionId 只在本地有效，Redis 广播按 userId 匹配
        RedisWebSocketMessage msg = new RedisWebSocketMessage(null, sessionId, messageType, messageContent);
        redisTopic.publish(JsonUtils.toJsonString(msg));
    }

    @Override
    public void sendToAll(String messageType, String messageContent) {
        RedisWebSocketMessage msg = new RedisWebSocketMessage(null, null, messageType, messageContent);
        redisTopic.publish(JsonUtils.toJsonString(msg));
    }
}
