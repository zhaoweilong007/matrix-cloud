package com.matrix.resource;

import com.matrix.feign.annotation.EnableFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 资源服务启动类。
 *
 * <p>负责 OSS、SMS、邮件等服务。</p>
 */
@EnableDiscoveryClient
@EnableFeign
@SpringBootApplication
@Slf4j
public class ResourceApp {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApp.class, args);
        log.info("resource start success");
    }
}
