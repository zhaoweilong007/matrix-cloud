package com.matrix.mq.redis.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Redis MQ 运行参数。 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "matrix.mq.redis")
public class RedisMqProperties {
    @Min(1)
    private int streamBatchSize = 10;
    @Min(1)
    private int pendingScanLimit = 100;
    @Min(1)
    private long pendingExpireMillis = 5 * 60 * 1000L;
    @Min(1)
    private long streamMaxLength = 10_000L;
    private String pendingRetryCron = "35 * * * * ?";
    private String cleanupCron = "0 0 * * * ?";
}
