package com.matrix;

import com.matrix.feign.annotation.EnableFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述：网关服务
 *
 * @author zwl
 * @since ${DATE} ${TIME}
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeign
@Slf4j
public class GatewayApp {
    public static void main(String[] args) {
        System.setProperty("csp.sentinel.app.type", "1");
        SpringApplication.run(GatewayApp.class, args);
        log.info("✅ gateway start success");
    }
}
