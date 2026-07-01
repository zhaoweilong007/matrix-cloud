package com.matrix.feign.annotation;

import com.matrix.feign.config.FeignAutoConfig;
import java.lang.annotation.*;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * Feign注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(basePackages = {"com.matrix.*.api.**.client", "com.matrix.web.client"})
@Import(FeignAutoConfig.class)
public @interface EnableFeign {}
