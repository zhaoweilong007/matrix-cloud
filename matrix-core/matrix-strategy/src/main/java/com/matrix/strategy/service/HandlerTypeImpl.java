package com.matrix.strategy.service;

import com.matrix.strategy.annonation.HandlerType;

import java.lang.annotation.Annotation;

/**
 * 策略模型业务类型注解实现类
 */
public class HandlerTypeImpl implements HandlerType {

    private final String type;
    private final String source;

    HandlerTypeImpl(String type, String source) {
        this.source = source;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        hashCode += (127 * "type".hashCode()) ^ type.hashCode();
        hashCode += (127 * "source".hashCode()) ^ source.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HandlerType other)) {
            return false;
        }
        return type.equals(other.type()) && source.equals(other.source());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return HandlerType.class;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String source() {
        return source;
    }
}
