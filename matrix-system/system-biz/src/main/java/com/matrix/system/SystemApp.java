package com.matrix.system;

import com.matrix.feign.annotation.EnableFeign;
import lombok.extern.slf4j.Slf4j;
import org.dromara.trans.config.TransServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 系统服务启动类。
 *
 * <p>提供用户、角色、菜单、资源、租户等系统管理功能。</p>
 */
@EnableDiscoveryClient
@EnableFeign
@SpringBootApplication(exclude = {TransServiceConfig.class})
@Slf4j
public class SystemApp {

    public static void main(String[] args) {
        SpringApplication.run(SystemApp.class, args);
        log.info("✅ system start success");
    }
}
