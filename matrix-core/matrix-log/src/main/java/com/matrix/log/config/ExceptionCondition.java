package com.matrix.log.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author owen
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
