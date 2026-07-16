package com.matrix.auto.properties;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

/** 异步任务线程池配置属性。 */
@Setter
@Getter
@RefreshScope
@Validated
@ConfigurationProperties(prefix = "matrix.async-task")
public class AsyncTaskProperties {
    @Min(1)
    private Integer corePoolSize;
    @Min(1)
    private Integer maxPoolSize;
    @Min(0)
    private Integer queueCapacity;
    private String threadNamePrefix;
}
