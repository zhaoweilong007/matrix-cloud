package com.matrix.strategy.annonation;

import java.lang.annotation.*;
import org.springframework.stereotype.Service;

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
