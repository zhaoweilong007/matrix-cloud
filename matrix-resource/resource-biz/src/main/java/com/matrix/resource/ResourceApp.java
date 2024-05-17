package com.matrix.resource;

import com.matrix.feign.annotation.EnableFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述：负责oss、sms、email等服务
 *
 * @author zwl
 * @since 2022/11/23 16:12
 **/
@EnableDiscoveryClient
@EnableFeign
@SpringBootApplication
public class ResourceApp {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApp.class, args);
    }
}
