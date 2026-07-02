package com.matrix.web.feature;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 功能开关 AOP 切面。
 *
 * <p>拦截标注 {@link FeatureToggle} 的方法，根据
 * {@link FeatureToggleProperties} 中的配置决定是否放行。</p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleProperties properties;

    /**
     * 方法级别 {@link FeatureToggle} 拦截。
     */
    @Around("@annotation(featureToggle)")
    public Object around(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        String featureName = featureToggle.value();

        if (properties.isEnabled(featureName)) {
            return joinPoint.proceed();
        }

        log.debug("功能 [{}] 已关闭, 拦截方法: {}#{}",
                featureName,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        throw new ServiceException(SystemErrorTypeEnum.OPERATE_FAIL, featureToggle.message());
    }
}
