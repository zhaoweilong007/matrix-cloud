package com.matrix.websocket.core.sender;

import com.matrix.common.util.json.JsonUtils;

/**
 * WebSocket 消息发送器 SPI 接口。
 *
 * <p>支持三种发送粒度：指定用户、指定用户类型（广播）、指定 SessionId。</p>
 *
 */
public interface WebSocketMessageSender {

    /**
     * 向指定用户的所有连接发送消息。
     *
     * @param userId         用户 ID
     * @param messageType    消息类型
     * @param messageContent 消息内容（JSON 字符串）
     */
    void send(Long userId, String messageType, String messageContent);

    /**
     * 向指定 Session 发送消息。
     *
     * @param sessionId      会话 ID
     * @param messageType    消息类型
     * @param messageContent 消息内容（JSON 字符串）
     */
    void sendBySessionId(String sessionId, String messageType, String messageContent);

    /**
     * 向所有在线用户广播消息。
     *
     * @param messageType    消息类型
     * @param messageContent 消息内容（JSON 字符串）
     */
    void sendToAll(String messageType, String messageContent);

    /**
     * 向指定用户发送对象消息（自动序列化为 JSON）。
     *
     * @param userId      用户 ID
     * @param messageType 消息类型
     * @param content     消息对象
     */
    default void sendObject(Long userId, String messageType, Object content) {
        send(userId, messageType, JsonUtils.toJsonString(content));
    }

    /**
     * 广播对象消息。
     *
     * @param messageType 消息类型
     * @param content     消息对象
     */
    default void sendObjectToAll(String messageType, Object content) {
        sendToAll(messageType, JsonUtils.toJsonString(content));
    }
}
