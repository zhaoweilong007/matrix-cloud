package com.matrix.websocket.core.message;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket JSON 消息帧格式。
 *
 * <p>所有通过 WebSocket 传输的消息都使用此格式：</p>
 * <pre>
 * {
 *   "type": "demo-message",
 *   "content": "{\"field1\":\"value1\"}"
 * }
 * </pre>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonWebSocketMessage implements Serializable {

    /** 消息类型，用于分发到对应的 {@link com.matrix.websocket.core.listener.WebSocketMessageListener} */
    private String type;

    /** 消息内容（JSON 字符串） */
    private String content;
}
