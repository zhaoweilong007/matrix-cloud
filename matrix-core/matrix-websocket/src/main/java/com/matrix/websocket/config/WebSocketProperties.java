package com.matrix.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSocket 配置属性。
 *
 */
@Data
@ConfigurationProperties(prefix = "matrix.websocket")
public class WebSocketProperties {

    /** WebSocket 连接路径，默认 /ws */
    private String path = "/ws";

    /** 消息发送器类型：local / redis，默认 local */
    private String senderType = "local";

    /** 允许的跨域源 */
    private String allowedOrigins = "*";

    private Sse sse = new Sse();

    @Data
    public static class Sse {
        private long timeoutMillis = 0L;
        private int maxConnections = 10_000;
        private int maxConnectionsPerUser = 10;
    }
}
