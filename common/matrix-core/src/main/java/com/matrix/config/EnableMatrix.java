package com.matrix.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
@EnableFeignClients
@MapperScan("com.matrix.**.mapper")
public @interface EnableMatrix {

}
