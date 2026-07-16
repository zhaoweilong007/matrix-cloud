package com.matrix.strategy.service;

import com.matrix.strategy.annonation.HandlerType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 业务处理策略选择器
 */
public class BusinessHandlerChooser {

    private Map<String, BusinessHandler> businessHandlerMap;

    public void setBusinessHandlerMap(List<BusinessHandler> orderHandlers) {
        // 注入各类型的订单处理类，并过滤掉无有效策略标识的 Bean 规避 NullPointerException
        businessHandlerMap = orderHandlers.stream()
                .filter(handler -> handler instanceof IHandlerType
                        || AnnotationUtils.findAnnotation(handler.getClass(), HandlerType.class) != null)
                .collect(Collectors.toMap(
                        orderHandler -> {
                            if (orderHandler instanceof IHandlerType handler) {
                                return getHandlerKey(handler.type(), handler.source());
                            }
                            HandlerType annotation = AnnotationUtils.findAnnotation(orderHandler.getClass(), HandlerType.class);
                            return getHandlerKey(annotation.type(), annotation.source());
                        },
                        v -> v,
                        (v1, v2) -> v1));
    }

    public <R, T> BusinessHandler<R, T> businessHandlerChooser(String type, String source) {
        return businessHandlerMap.get(getHandlerKey(type, source));
    }

    private String getHandlerKey(String type, String source) {
        return type + ":" + source;
    }
}
