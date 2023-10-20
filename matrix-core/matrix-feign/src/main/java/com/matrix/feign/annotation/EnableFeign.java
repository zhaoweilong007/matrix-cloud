package com.matrix.feign.annotation;

import com.matrix.feign.config.FeignAutoConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Feign注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(basePackages = "com.matrix.*.api.**.client")
@Import(FeignAutoConfig.class)
public @interface EnableFeign {

}
