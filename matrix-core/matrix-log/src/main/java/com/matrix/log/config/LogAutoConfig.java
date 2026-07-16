package com.matrix.log.config;

import com.matrix.auto.properties.ExceptionNoticeProperties;
import com.matrix.log.api.client.RemoteLogService;
import com.matrix.log.aspect.ExceptionNoticeAspect;
import com.matrix.log.aspect.LogAspect;
import com.matrix.log.event.LogEventListener;
import io.github.linpeilie.Converter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 **/
@AutoConfiguration
@EnableConfigurationProperties(ExceptionNoticeProperties.class)
public class LogAutoConfig {

    /**
     * LogAspect 不再依赖 RemoteUserService（已移除同步 Feign 调用）
     */
    @Bean
    @ConditionalOnBean(RemoteLogService.class)
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public ExceptionNoticeAspect exceptionNoticeAspect() {
        return new ExceptionNoticeAspect();
    }

    @Bean
    @ConditionalOnBean({RemoteLogService.class})
    public LogEventListener logEventListener(
            ExceptionNoticeProperties noticeProperties, RemoteLogService remoteLogService, Converter converter) {
        return new LogEventListener(noticeProperties, remoteLogService, converter);
    }
}
