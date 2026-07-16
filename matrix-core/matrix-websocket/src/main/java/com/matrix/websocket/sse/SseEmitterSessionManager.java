package com.matrix.websocket.sse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE（Server-Sent Events）会话管理器。
 *
 * <p>管理与客户端的 SSE 长连接，支持按用户+Token 维度定位连接，
 * 内置心跳检测和失效连接清理。</p>
 *
 */
@Slf4j
public class SseEmitterSessionManager {

    /**
     * 用户 SSE 连接池：userId → (token → SseEmitter)
     */
    private static final Map<Long, Map<String, SseEmitter>> USER_TOKEN_EMITTERS = new ConcurrentHashMap<>();

    /**
     * SSE 超时时间（毫秒），默认 0 表示无超时
     */
    private final long timeout;
    private final int maxConnections;
    private final int maxConnectionsPerUser;

    public SseEmitterSessionManager() {
        this(0L, 10_000, 10);
    }

    public SseEmitterSessionManager(long timeout) {
        this(timeout, 10_000, 10);
    }

    public SseEmitterSessionManager(long timeout, int maxConnections, int maxConnectionsPerUser) {
        this.timeout = timeout;
        this.maxConnections = maxConnections;
        this.maxConnectionsPerUser = maxConnectionsPerUser;
    }

    /**
     * 建立与指定用户的 SSE 连接。
     *
     * @param userId 用户 ID
     * @param token  连接令牌（用于区分同一用户的多设备连接）
     * @return SseEmitter 实例
     */
    public SseEmitter connect(Long userId, String token) {
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.computeIfAbsent(userId,
                k -> new ConcurrentHashMap<>());

        if (!emitters.containsKey(token) && emitters.size() >= maxConnectionsPerUser) {
            throw new IllegalStateException("SSE connections exceed per-user limit");
        }
        if (!emitters.containsKey(token) && getActiveConnectionCount() >= maxConnections) {
            throw new IllegalStateException("SSE connections exceed global limit");
        }

        // 关闭同 token 的旧连接（防止重复连接）
        SseEmitter oldEmitter = emitters.remove(token);
        if (oldEmitter != null) {
            try {
                oldEmitter.complete();
            } catch (Exception ignore) {
            }
        }

        SseEmitter emitter = new SseEmitter(timeout);
        emitters.put(token, emitter);

        // 生命周期回调：完成后清理
        emitter.onCompletion(() -> cleanSession(userId, token));
        emitter.onTimeout(() -> cleanSession(userId, token));
        emitter.onError(e -> cleanSession(userId, token));

        // 发送连接成功事件
        try {
            emitter.send(SseEmitter.event().comment("connected"));
        } catch (IOException e) {
            emitters.remove(token);
        }
        return emitter;
    }

    /**
     * 断开指定用户的 SSE 连接。
     */
    public void disconnect(Long userId, String token) {
        if (userId == null || token == null) {
            return;
        }
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.get(userId);
        if (emitters != null) {
            SseEmitter emitter = emitters.get(token);
            if (emitter != null) {
                try {
                    emitter.complete();
                } catch (Exception ignore) {
                }
            }
            cleanSession(userId, token);
        }
    }

    /**
     * 断开指定用户的所有 SSE 连接。
     */
    public void disconnect(Long userId) {
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.remove(userId);
        if (MapUtil.isNotEmpty(emitters)) {
            emitters.values().forEach(emitter -> {
                try {
                    emitter.complete();
                } catch (Exception ignore) {
                }
            });
        }
    }

    /**
     * 向指定用户的所有 SSE 连接发送消息。
     *
     * @param userId  用户 ID
     * @param message 消息内容
     */
    public void sendMessage(Long userId, String message) {
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.get(userId);
        if (MapUtil.isNotEmpty(emitters)) {
            List<String> toRemove = new ArrayList<>();
            emitters.forEach((token, emitter) -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data(message));
                } catch (Exception e) {
                    toRemove.add(token);
                    try {
                        emitter.complete();
                    } catch (Exception ignore) {
                    }
                }
            });
            toRemove.forEach(emitters::remove);
            if (emitters.isEmpty()) {
                USER_TOKEN_EMITTERS.remove(userId);
            }
        }
    }

    /**
     * 向所有连接的 SSE 会话广播消息。
     *
     * @param message 消息内容
     */
    public void sendMessageToAll(String message) {
        List<Long> userIds = new ArrayList<>(USER_TOKEN_EMITTERS.keySet());
        userIds.forEach(userId -> sendMessage(userId, message));
    }

    /**
     * 心跳检测：向所有连接发送心跳，清理失效连接。
     */
    public void heartbeat() {
        SseEmitter.SseEventBuilder heartbeat = SseEmitter.event().comment("heartbeat");
        List<Long> toRemoveUsers = new ArrayList<>();

        USER_TOKEN_EMITTERS.forEach((userId, emitterMap) -> {
            if (CollUtil.isEmpty(emitterMap)) {
                toRemoveUsers.add(userId);
                return;
            }
            emitterMap.entrySet().removeIf(entry -> {
                try {
                    entry.getValue().send(heartbeat);
                    return false;
                } catch (Exception ex) {
                    try {
                        entry.getValue().complete();
                    } catch (Exception ignore) {
                    }
                    return true;
                }
            });
            if (emitterMap.isEmpty()) {
                toRemoveUsers.add(userId);
            }
        });
        toRemoveUsers.forEach(USER_TOKEN_EMITTERS::remove);
    }

    /**
     * 获取当前活跃连接数
     *
     * @return 活跃连接总数
     */
    public int getActiveConnectionCount() {
        return USER_TOKEN_EMITTERS.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    /**
     * 原子地清理失效会话，防止高并发 connect/disconnect 产生孤儿 Map 导致连接泄露
     */
    private void cleanSession(Long userId, String token) {
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.get(userId);
        if (emitters != null) {
            emitters.remove(token);
            // 使用 computeIfPresent 的原子保证：如果 map 为空则将其从全局连接池中彻底移除
            USER_TOKEN_EMITTERS.computeIfPresent(userId, (key, currentMap) -> {
                return currentMap.isEmpty() ? null : currentMap;
            });
        }
    }
}
