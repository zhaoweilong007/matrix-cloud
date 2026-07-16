package com.matrix.web.config;

import com.matrix.auto.properties.AsyncTaskProperties;
import com.matrix.auto.properties.AsycTaskProperties;
import com.matrix.common.thread.CustomThreadPoolTaskExecutor;
import com.matrix.web.thread.ContextCopyingDecorator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义异步任务配置
 */
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({AsyncTaskProperties.class, AsycTaskProperties.class})
@AutoConfiguration
public class DefaultAsycTaskConfig {

    /**
     * 创建异步任务线程池
     */
    @Bean
    @ConditionalOnMissingBean(name = "taskExecutor")
    public TaskExecutor taskExecutor(AsyncTaskProperties asyncTaskProperties, AsycTaskProperties legacyProperties) {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(firstNonNull(asyncTaskProperties.getCorePoolSize(), legacyProperties.getCorePoolSize(), 10));
        executor.setMaxPoolSize(firstNonNull(asyncTaskProperties.getMaxPoolSize(), legacyProperties.getMaxPoolSize(), 200));
        executor.setQueueCapacity(firstNonNull(asyncTaskProperties.getQueueCapacity(), legacyProperties.getQueueCapacity(), 10));
        executor.setThreadNamePrefix(firstNonNull(asyncTaskProperties.getThreadNamePrefix(), legacyProperties.getThreadNamePrefix(), "matrixExecutor-"));
        // for passing in request scope context
        executor.setTaskDecorator(new ContextCopyingDecorator());
        /*
           rejection-policy：当pool已经达到max size的时候，如何处理新任务
           CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    private static <T> T firstNonNull(T preferredValue, T fallbackValue, T defaultValue) {
        return preferredValue != null ? preferredValue : fallbackValue != null ? fallbackValue : defaultValue;
    }
}
