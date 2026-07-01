package com.matrix.websocket.core.sender.redis;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Redis 广播消息体。
 *
 * @author matrix
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisWebSocketMessage implements Serializable {

    /** 目标用户 ID（null 表示广播给所有） */
    private Long userId;

    /** 目标 Session ID（null 表示按 userId 匹配） */
    private String sessionId;

    /** 消息类型 */
    private String messageType;

    /** 消息内容（JSON 字符串） */
    private String messageContent;
}
