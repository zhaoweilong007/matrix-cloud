package com.matrix.auto.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties(prefix = "matrix.asyc-task")
@RefreshScope
public class AsycTaskProperties {
    /**
     * 线程池维护线程的最小数量.
     */
    private int corePoolSize = 10;
    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = 200;
    /**
     * 队列最大长度
     */
    private int queueCapacity = 10;
    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "matrixExecutor-";
}
