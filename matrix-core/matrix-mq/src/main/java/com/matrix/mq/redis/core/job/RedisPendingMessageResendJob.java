package com.matrix.mq.redis.core.job;

import com.matrix.mq.redis.core.RedisMqTemplate;
import com.matrix.mq.redis.config.RedisMqProperties;
import com.matrix.mq.redis.stream.AbstractRedisStreamMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Redis Stream 待处理消息重发 Job。
 *
 * <p>按配置周期检查各 Stream 的 Pending Entries List (PEL)，
 * 对超时未确认消息记录告警，供运维处理或由消费者恢复机制接管。</p>
 */
@Slf4j
@RequiredArgsConstructor
public class RedisPendingMessageResendJob {

    private static final String LOCK_KEY = "redis:stream:pending-message-resend:lock";

    private final RedisMqTemplate redisMqTemplate;
    private final RedissonClient redissonClient;
    private final RedisMqProperties properties;
    private final List<AbstractRedisStreamMessageListener<?>> listeners;

    /**
     * 每分钟第 35 秒执行。
     */
    @Scheduled(cron = "${matrix.mq.redis.pending-retry-cron:35 * * * * ?}")
    public void resend() {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        if (!lock.tryLock()) {
            return;
        }
        try {
            StringRedisTemplate redisTemplate = redisMqTemplate.getRedisTemplate();
            for (AbstractRedisStreamMessageListener<?> listener : listeners) {
                String streamKey = listener.getStreamKey();
                String group = listener.getGroup();
                try {
                    PendingMessages pendingMessages = redisTemplate.opsForStream()
                            .pending(streamKey, group, null, properties.getPendingScanLimit());
                    for (PendingMessage pendingMessage : pendingMessages) {
                        long elapsedMs = pendingMessage.getElapsedTimeSinceLastDelivery().toMillis();
                        if (elapsedMs > properties.getPendingExpireMillis()) {
                            // 不确认或转交消息，避免在没有幂等语义时重复投递。
                            // 此处仅记录告警，实际恢复策略由业务消费者决定。
                            log.warn("[RedisMQ] 待处理消息超时 streamKey=[{}] messageId=[{}] elapsedMs=[{}]",
                                    streamKey, pendingMessage.getIdAsString(), elapsedMs);
                        }
                    }
                } catch (Exception e) {
                    log.error("[RedisMQ] 检查待处理消息失败 streamKey=[{}]", streamKey, e);
                }
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
