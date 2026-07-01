package com.matrix.websocket.core.listener;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 消息监听器 SPI 接口。
 *
 * <p>业务方实现此接口并注册为 Spring Bean 即可自动接收对应类型的消息。
 * 泛型 T 为消息内容反序列化后的类型。</p>
 *
 * <pre>
 * // 示例：处理 "demo-message" 类型的消息
 * {@literal @}Component
 * public class DemoMessageListener implements WebSocketMessageListener&lt;DemoMessage&gt; {
 *     {@literal @}Override
 *     public String getType() { return "demo-message"; }
 *     {@literal @}Override
 *     public void onMessage(WebSocketSession session, DemoMessage message) {
 *         // 处理消息
 *     }
 * }
 * </pre>
 *
 * @param <T> 消息内容类型
 * @author matrix
 */
public interface WebSocketMessageListener<T> {

    /**
     * 收到消息回调。
     *
     * @param session WebSocket 会话
     * @param message 消息对象
     */
    void onMessage(WebSocketSession session, T message);

    /**
     * 返回此监听器处理的消息类型。
     *
     * @return 消息类型标识
     */
    String getType();
}
