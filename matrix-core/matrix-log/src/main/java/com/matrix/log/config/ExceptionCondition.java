package com.matrix.log.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 异常通知条件装配类，用于判断是否启用异常通知功能。
 * <p>
 * 当环境中配置了 {@code matrix.exception.notice.alertUrl} 属性时，
 * 条件匹配成功，相关异常通知 Bean 才会被注册。
 */
public class ExceptionCondition implements Condition {

    /**
     * 条件装配
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().containsProperty("matrix.exception.notice.alertUrl");
    }
}
