package com.matrix.mq.redis.core.job;

import com.matrix.mq.redis.core.RedisMqTemplate;
import com.matrix.mq.redis.stream.AbstractRedisStreamMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Redis Stream 消息清理 Job。
 *
 * <p>每小时执行一次，对每个 Stream 执行 {@code XTRIM MAXLEN} 操作，
 * 限制 Stream 最大长度，防止已消费消息无限增长消耗 Redis 内存。</p>
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamMessageCleanupJob {

    /** Stream 最大保留消息数 */
    private static final long MAX_LEN = 10000;
    private static final String LOCK_KEY = "redis:stream:message-cleanup:lock";

    private final RedisMqTemplate redisMqTemplate;
    private final RedissonClient redissonClient;
    private final List<AbstractRedisStreamMessageListener<?>> listeners;

    /**
     * 每小时整点执行。
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanup() {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        if (!lock.tryLock()) {
            return;
        }
        try {
            StringRedisTemplate redisTemplate = redisMqTemplate.getRedisTemplate();
            for (AbstractRedisStreamMessageListener<?> listener : listeners) {
                try {
                    Long trimmed = redisTemplate.opsForStream().trim(listener.getStreamKey(), MAX_LEN, false);
                    if (trimmed != null && trimmed > 0) {
                        log.info("[RedisMQ] Stream 清理完成 streamKey=[{}] trimmed=[{}]", listener.getStreamKey(), trimmed);
                    }
                } catch (Exception e) {
                    log.error("[RedisMQ] Stream 清理失败 streamKey=[{}]", listener.getStreamKey(), e);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
