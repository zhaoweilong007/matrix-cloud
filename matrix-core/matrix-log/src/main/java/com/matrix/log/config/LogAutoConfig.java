package com.matrix.log.config;

import com.matrix.auto.properties.ExceptionNoticeProperties;
import com.matrix.log.api.client.RemoteLogService;
import com.matrix.log.api.client.RemoteUserService;
import com.matrix.log.aspect.ExceptionNoticeAspect;
import com.matrix.log.aspect.LogAspect;
import com.matrix.log.event.LogEventListener;
import io.github.linpeilie.Converter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/9
 **/

@AutoConfiguration
@EnableConfigurationProperties(ExceptionNoticeProperties.class)
public class LogAutoConfig {

    @Bean
    @ConditionalOnBean(RemoteUserService.class)
    public LogAspect logAspect(RemoteUserService remoteUserService) {
        return new LogAspect(remoteUserService);
    }

    @Bean
    public ExceptionNoticeAspect exceptionNoticeAspect() {
        return new ExceptionNoticeAspect();
    }

    @Bean
    @ConditionalOnBean({RemoteUserService.class})
    public LogEventListener logEventListener(ExceptionNoticeProperties noticeProperties,
                                             RemoteLogService remoteLogService,
                                             Converter converter) {
        return new LogEventListener(noticeProperties, remoteLogService, converter);
    }
}
