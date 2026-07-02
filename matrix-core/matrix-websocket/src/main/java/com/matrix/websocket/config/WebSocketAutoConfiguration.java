package com.matrix.websocket.config;

import com.matrix.common.model.login.LoginUser;
import com.matrix.websocket.core.handler.JsonWebSocketMessageHandler;
import com.matrix.websocket.core.listener.WebSocketMessageListener;
import com.matrix.websocket.core.sender.WebSocketMessageSender;
import com.matrix.websocket.core.sender.local.LocalWebSocketMessageSender;
import com.matrix.websocket.core.sender.redis.RedisWebSocketMessageConsumer;
import com.matrix.websocket.core.sender.redis.RedisWebSocketMessageSender;
import com.matrix.websocket.core.session.WebSocketSessionHandlerDecorator;
import com.matrix.websocket.core.session.WebSocketSessionManager;
import com.matrix.websocket.core.session.WebSocketSessionManagerImpl;
import com.matrix.websocket.core.util.WebSocketFrameworkUtils;
import com.matrix.websocket.sse.SseEmitterSessionManager;
import java.util.List;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * WebSocket 自动配置。
 *
 * <p>支持两种发送模式：</p>
 * <ul>
 *   <li>local — 单节点，直接本地投递</li>
 *   <li>redis — 多节点，通过 Redis Pub/Sub 广播</li>
 * </ul>
 *
 */
@AutoConfiguration
@EnableWebSocket
@EnableConfigurationProperties(WebSocketProperties.class)
@ConditionalOnProperty(prefix = "matrix.websocket", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebSocketAutoConfiguration implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketAutoConfiguration.class);

    /**
     * WebSocket 配置属性
     */
    private final WebSocketProperties webSocketProperties;
    /**
     * WebSocket Session 管理器
     */
    private final WebSocketSessionManager sessionManager;
    /**
     * WebSocket 消息处理器
     */
    private final WebSocketHandler webSocketHandler;

    public WebSocketAutoConfiguration(WebSocketProperties webSocketProperties,
            WebSocketSessionManager sessionManager,
            WebSocketHandler webSocketHandler) {
        this.webSocketProperties = webSocketProperties;
        this.sessionManager = sessionManager;
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * 注册 WebSocket 处理器路径、拦截器和跨域配置
     *
     * @param registry WebSocket 处理器注册中心
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, webSocketProperties.getPath())
                .addInterceptors(webSocketHandshakeInterceptor())
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins());
        log.info("WebSocket registered at path: {}", webSocketProperties.getPath());
    }

    // ========== Bean 定义 ==========

    /**
     * 创建 WebSocket Session 管理器 Bean
     *
     * @return WebSocketSessionManager 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebSocketSessionManager webSocketSessionManager() {
        return new WebSocketSessionManagerImpl();
    }

    /**
     * 创建 WebSocket 消息处理器，注册所有消息监听器并包装 Session 生命周期装饰器
     *
     * @param listeners      消息监听器列表
     * @param sessionManager Session 管理器
     * @return WebSocketHandler 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandler webSocketHandler(List<WebSocketMessageListener<?>> listeners,
            WebSocketSessionManager sessionManager) {
        JsonWebSocketMessageHandler handler = new JsonWebSocketMessageHandler(
                listeners != null ? listeners : List.of());
        return new WebSocketSessionHandlerDecorator(handler, sessionManager);
    }

    /**
     * 创建 WebSocket 握手拦截器，从 Sa-Token 上下文中提取登录用户并注入 Session 属性
     *
     * @return HandshakeInterceptor 实例
     */
    @Bean
    public HandshakeInterceptor webSocketHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(org.springframework.http.server.ServerHttpRequest request,
                    org.springframework.http.server.ServerHttpResponse response,
                    WebSocketHandler wsHandler, java.util.Map<String, Object> attributes) {
                // 尝试从请求中获取认证信息
                // 子模块可通过实现 HandshakeInterceptor 并覆盖此 Bean 来定制认证逻辑
                try {
                    // 尝试从 Sa-Token 上下文获取登录用户（反射调用，避免硬依赖）
                    Object loginUser = getLoginUserFromContext();
                    if (loginUser instanceof LoginUser user) {
                        WebSocketFrameworkUtils.setLoginUser(user, attributes);
                    }
                } catch (Exception ignored) {
                    // 未找到登录用户，允许匿名连接
                }
                return true;
            }

            @Override
            public void afterHandshake(org.springframework.http.server.ServerHttpRequest request,
                    org.springframework.http.server.ServerHttpResponse response,
                    WebSocketHandler wsHandler, Exception exception) {
            }

            private Object getLoginUserFromContext() {
                try {
                    // 反射调用 Sa-Token StpUtil.getSession().get("loginUser")
                    Class<?> stpUtilClass = Class.forName("cn.dev33.satoken.stp.StpUtil");
                    Object tokenSession = stpUtilClass.getMethod("getSession").invoke(null);
                    if (tokenSession != null) {
                        return tokenSession.getClass().getMethod("get", String.class).invoke(tokenSession, "loginUser");
                    }
                } catch (Exception ignored) {
                }
                return null;
            }
        };
    }

    // ========== Local Sender（默认） ==========

    /**
     * 创建本地 WebSocket 消息发送器（单节点模式，默认）
     *
     * @param sessionManager Session 管理器
     * @return LocalWebSocketMessageSender 实例
     */
    @Bean
    @ConditionalOnMissingBean(WebSocketMessageSender.class)
    @ConditionalOnProperty(prefix = "matrix.websocket", name = "sender-type", havingValue = "local", matchIfMissing = true)
    public WebSocketMessageSender localWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        log.info("Using Local WebSocket message sender (single-node mode)");
        return new LocalWebSocketMessageSender(sessionManager);
    }

    // ========== Redis Sender（多节点） ==========

    /**
     * 创建 Redis 广播 WebSocket 消息发送器（多节点模式）
     *
     * @param sessionManager  Session 管理器
     * @param redissonClient  Redisson 客户端
     * @return RedisWebSocketMessageSender 实例
     */
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "matrix.websocket", name = "sender-type", havingValue = "redis")
    public WebSocketMessageSender redisWebSocketMessageSender(WebSocketSessionManager sessionManager,
            RedissonClient redissonClient) {
        log.info("Using Redis WebSocket message sender (multi-node mode)");
        return new RedisWebSocketMessageSender(sessionManager, redissonClient);
    }

    /**
     * 创建 Redis WebSocket 消息消费者，订阅 Channel 接收其他节点的广播消息
     *
     * @param sessionManager  Session 管理器
     * @param redissonClient  Redisson 客户端
     * @return RedisWebSocketMessageConsumer 实例
     */
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "matrix.websocket", name = "sender-type", havingValue = "redis")
    public RedisWebSocketMessageConsumer redisWebSocketMessageConsumer(WebSocketSessionManager sessionManager,
            RedissonClient redissonClient) {
        return new RedisWebSocketMessageConsumer(redissonClient, sessionManager);
    }

    // ========== SSE 支持 ==========

    /**
     * 创建 SSE 会话管理器
     *
     * @return SseEmitterSessionManager 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "matrix.websocket.sse", name = "enabled", havingValue = "true")
    public SseEmitterSessionManager sseEmitterSessionManager() {
        log.info("SSE support enabled");
        return new SseEmitterSessionManager();
    }
}
