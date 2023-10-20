package com.matrix.strategy.annonation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 策略模式注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface HandlerType {

    String type();

    String source();
}
