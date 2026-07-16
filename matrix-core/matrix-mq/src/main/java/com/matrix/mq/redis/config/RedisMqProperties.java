package com.matrix.mq.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Redis MQ 运行参数。 */
@Getter
@Setter
@ConfigurationProperties(prefix = "matrix.mq.redis")
public class RedisMqProperties {
    private int streamBatchSize = 10;
    private int pendingScanLimit = 100;
    private long pendingExpireMillis = 5 * 60 * 1000L;
    private long streamMaxLength = 10_000L;
    private String pendingRetryCron = "35 * * * * ?";
    private String cleanupCron = "0 0 * * * ?";
}
