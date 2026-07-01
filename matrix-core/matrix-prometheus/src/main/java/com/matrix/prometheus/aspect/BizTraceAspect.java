package com.matrix.prometheus.aspect;

import cn.hutool.core.util.StrUtil;
import com.matrix.common.util.spring.SpringExpressionUtils;
import com.matrix.prometheus.annotation.BizTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

/**
 * {@link BizTrace} 切面，在业务方法上创建带业务标签的追踪 Span。
 *
 * <p>通过 SkyWalking 原生 API 创建本地 Span，并设置 {@code biz.type} 和
 * {@code biz.id} 标签，便于在 SkyWalking UI 中按业务维度搜索调用链。</p>
 *
 * @author matrix
 */
@Aspect
@Slf4j
public class BizTraceAspect {

    @Around(value = "@annotation(bizTrace)")
    @Trace(operationName = "BizTrace")
    public Object around(ProceedingJoinPoint joinPoint, BizTrace bizTrace) throws Throwable {
        try {
            // 设置业务标签
            setBizTag(bizTrace, joinPoint);
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            ActiveSpan.error(throwable);
            throw throwable;
        }
    }

    private void setBizTag(BizTrace trace, ProceedingJoinPoint joinPoint) {
        try {
            Map<String, Object> result = SpringExpressionUtils.parseExpressions(
                    joinPoint, java.util.Arrays.asList(trace.type(), trace.id()));
            ActiveSpan.tag(BizTrace.TYPE_TAG, StrUtil.blankToDefault(
                    String.valueOf(result.getOrDefault(trace.type(), "")), ""));
            ActiveSpan.tag(BizTrace.ID_TAG, StrUtil.blankToDefault(
                    String.valueOf(result.getOrDefault(trace.id(), "")), ""));
        } catch (Exception ex) {
            log.warn("[BizTrace] 解析 bizType/bizId SpEL 表达式失败", ex);
        }
    }
}
