package com.matrix.sentinel;

import cn.hutool.core.util.StrUtil;
import net.dreamlu.mica.auto.annotation.AutoEnvPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 针对sentinel的环境配置
 *
 * @author ZhaoWeiLong
 * @since 2023/7/17
 **/
@AutoEnvPostProcessor
public class SentinelEnvironment implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String serverPort = environment.getProperty("server.port");
        String clientIp = environment.getProperty("spring.cloud.nacos.discovery.ip");
        if (StrUtil.isNotBlank(serverPort)) {
            Integer transport = Integer.parseInt(serverPort) + 100;
            environment.getSystemProperties().put("spring.cloud.sentinel.transport.port", transport);
        }
        if (StrUtil.isNotBlank(clientIp)) {
            environment.getSystemProperties().put("spring.cloud.sentinel.transport.client-ip", clientIp);
        }
    }

}
