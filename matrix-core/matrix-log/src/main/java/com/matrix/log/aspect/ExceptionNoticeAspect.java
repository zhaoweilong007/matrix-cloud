package com.matrix.log.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.common.util.TracerUtils;
import com.matrix.common.util.spring.SpringUtils;
import com.matrix.log.annotation.ExceptionNoticeLog;
import com.matrix.log.config.ExceptionCondition;
import com.matrix.log.event.ExceptionEvent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 告警通知切面
 */
@Slf4j
@Aspect
@Conditional(ExceptionCondition.class)
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class})
public class ExceptionNoticeAspect {

    @Resource
    private ObjectMapper objectMapper;

    @After("@within(exceptionNoticeLog) || @annotation(exceptionNoticeLog)")
    public void beforeMethod(JoinPoint joinPoint, ExceptionNoticeLog exceptionNoticeLog) {
        try {
            Exception exception = Arrays.stream(joinPoint.getArgs())
                    .filter(Exception.class::isInstance)
                    .map(Exception.class::cast)
                    .findFirst()
                    .orElse(null);
            if (exception == null) {
                log.debug("Skipping exception notification without an exception argument: {}", joinPoint.getSignature());
                return;
            }
            ExceptionEvent event = this.getEvent(exception);
            SpringUtils.context().publishEvent(event);
        } catch (Exception exception) {
            log.warn("Failed to publish exception notification for {}", joinPoint.getSignature(), exception);
        }
    }

    /**
     * 构建异常事件
     */
    @SneakyThrows
    private ExceptionEvent getEvent(Exception exception) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String apiPath = attributes == null ? "unknown" : attributes.getRequest().getRequestURI();
        ExceptionEvent event = ExceptionEvent.builder().build();
        event.setApplication(Optional.ofNullable(SpringUtil.getProperty("spring.application.name"))
                .orElseGet(() -> "default"));
        event.setApiPath(apiPath);
        event.setTraceId(TracerUtils.getTraceId());
        event.setMessage(objectMapper.writeValueAsString(exception.getMessage()));
        event.setStackTrace(stackTrace(exception));
        return event;
    }

    private String stackTrace(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if (stackTrace.length == 0) {
            return DateUtil.formatDateTime(new Date()) + " [no stack trace]";
        }
        StackTraceElement callInfo = stackTrace[0];
        return DateUtil.formatDateTime(new Date()) + " [" + callInfo.getClassName() + "#"
                + callInfo.getMethodName() + "]-[" + callInfo.getLineNumber() + "]-["
                + Thread.currentThread().getName() + "] ";
    }
}
