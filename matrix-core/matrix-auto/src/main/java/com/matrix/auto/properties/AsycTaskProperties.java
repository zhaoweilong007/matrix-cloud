package com.matrix.auto.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 旧版异步任务线程池配置属性。
 *
 * @deprecated 请改用 {@code matrix.async-task}。
 */
@Setter
@Getter
@Deprecated(forRemoval = false)
@ConfigurationProperties(prefix = "matrix.asyc-task")
@RefreshScope
public class AsycTaskProperties {
    /**
     * 线程池维护线程的最小数量.
     */
    private Integer corePoolSize;
    /**
     * 线程池维护线程的最大数量
     */
    private Integer maxPoolSize;
    /**
     * 队列最大长度
     */
    private Integer queueCapacity;
    /**
     * 线程池前缀
     */
    private String threadNamePrefix;
}
