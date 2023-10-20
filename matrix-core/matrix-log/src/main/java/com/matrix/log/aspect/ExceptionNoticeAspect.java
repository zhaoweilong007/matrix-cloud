package com.matrix.log.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.common.util.SpringUtils;
import com.matrix.log.annotation.ExceptionNoticeLog;
import com.matrix.log.config.ExceptionCondition;
import com.matrix.log.event.ExceptionEvent;
import com.matrix.prometheus.util.TracerUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Optional;


/**
 * @author owen
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
            ExceptionEvent event = this.getEvent(joinPoint);
            SpringUtils.context().publishEvent(event);
        } catch (Exception ignored) {
        }
    }

    /**
     * 构建异常事件
     */
    @SneakyThrows
    private ExceptionEvent getEvent(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Exception exception = (Exception) args[0];
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        ExceptionEvent event = ExceptionEvent.builder().build();
        event.setApplication(Optional.ofNullable(SpringUtil.getProperty("spring.application.name")).orElseGet(() -> "default"));
        event.setApiPath(request.getRequestURI());
        event.setTraceId(TracerUtils.getTraceId());
        event.setMessage(objectMapper.writeValueAsString(exception.getMessage()));
        event.setStackTrace(stackTrace(exception));
        return event;
    }

    private String stackTrace(Exception exception) {
        try {
            StackTraceElement callInfo = exception.getStackTrace()[0];
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(DateUtil.formatDateTime(new Date())).append(" ")
                    .append("[" + callInfo.getClassName() + "#" + callInfo.getMethodName() + "]").append("-")
                    .append("[" + callInfo.getLineNumber() + "]").append("-")
                    .append("[" + Thread.currentThread().getName() + "]").append(" ");
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
