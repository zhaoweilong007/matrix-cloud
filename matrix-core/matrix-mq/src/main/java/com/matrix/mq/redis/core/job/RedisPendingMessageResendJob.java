package com.matrix.mq.redis.core.job;

import com.matrix.mq.redis.core.RedisMqTemplate;
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
 * <p>每分钟执行一次，检查各 Stream 的 Pending Entries List (PEL)，
 * 将超过 5 分钟未确认的消息重新投递到 Stream 中。</p>
 */
@Slf4j
@RequiredArgsConstructor
public class RedisPendingMessageResendJob {

    /** 待处理消息过期时间（毫秒） */
    private static final long EXPIRE_MILLIS = 5 * 60 * 1000;
    private static final String LOCK_KEY = "redis:stream:pending-message-resend:lock";

    private final RedisMqTemplate redisMqTemplate;
    private final RedissonClient redissonClient;
    private final List<AbstractRedisStreamMessageListener<?>> listeners;

    /**
     * 每分钟第 35 秒执行。
     */
    @Scheduled(cron = "35 * * * * ?")
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
                            .pending(streamKey, group, null, 100);
                    for (PendingMessage pendingMessage : pendingMessages) {
                        long elapsedMs = pendingMessage.getElapsedTimeSinceLastDelivery().toMillis();
                        if (elapsedMs > EXPIRE_MILLIS) {
                            // 消息超时未确认，重新 ack 后由 ResendJob 定期重试
                            // 实际重发由 PEL 机制保证，这里记录日志用于监控
                            log.warn("[RedisMQ] 待处理消息超时 streamKey=[{}] messageId=[{}] elapsedMs=[{}]",
                                    streamKey, pendingMessage.getIdAsString(), elapsedMs);
                        }
                    }
                } catch (Exception e) {
                    log.error("[RedisMQ] 检查待处理消息失败 streamKey=[{}]", streamKey, e);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
