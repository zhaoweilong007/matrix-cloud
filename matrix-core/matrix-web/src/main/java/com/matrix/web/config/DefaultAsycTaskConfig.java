package com.matrix.web.config;

import com.matrix.auto.properties.AsycTaskProperties;
import com.matrix.common.thread.CustomThreadPoolTaskExecutor;
import com.matrix.web.thread.ContextCopyingDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义异步任务配置
 */
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({AsycTaskProperties.class})
@AutoConfiguration
public class DefaultAsycTaskConfig {

    @Lazy
    @Autowired(required = false)
    private AsycTaskProperties asycTaskProperties;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(asycTaskProperties.getCorePoolSize());
        executor.setMaxPoolSize(asycTaskProperties.getMaxPoolSize());
        executor.setQueueCapacity(asycTaskProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asycTaskProperties.getThreadNamePrefix());
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
}
