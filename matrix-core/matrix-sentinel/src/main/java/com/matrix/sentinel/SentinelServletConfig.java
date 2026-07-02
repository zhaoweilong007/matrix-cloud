package com.matrix.sentinel;

import com.matrix.auto.factory.YamlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Sentinel Servlet 环境配置，注册 IP 解析器
 **/
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:sentinel.yml")
@Slf4j
public class SentinelServletConfig {

    /**
     * 注册 IP 来源解析器
     */
    @Bean
    public IpParse ipParse() {
        return new IpParse();
    }
}
