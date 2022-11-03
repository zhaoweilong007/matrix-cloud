package com.matrix.annotation;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.matrix.component.FeignInterceptor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;


/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 15:42
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@EnableDiscoveryClient
@EnableFeignClients(defaultConfiguration = FeignInterceptor.class)
@EnableAsync
@EnableScheduling
@EnableSpringUtil
public @interface EnableMatrix {

}
