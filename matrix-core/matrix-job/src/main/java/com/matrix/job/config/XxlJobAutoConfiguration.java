package com.matrix.job.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * XXL-JOB 执行器自动配置。
 *
 * <p>配合 {@code JobEnvironment} 自动计算执行器端口（server.port+200）和 IP。</p>
 *
 * @author matrix
 */
@AutoConfiguration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job", name = "admin-addresses")
public class XxlJobAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(XxlJobAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties properties) {
        log.info("Initializing XXL-JOB executor: admin={}, appName={}",
                properties.getAdminAddresses(), properties.getExecutor().getAppName());

        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(properties.getAdminAddresses());
        executor.setAppname(properties.getExecutor().getAppName());
        executor.setAccessToken(properties.getAccessToken());
        // 以下可在 JobEnvironment 中自动填充
        executor.setIp(properties.getExecutor().getIp());
        executor.setPort(properties.getExecutor().getPort() != null
                ? properties.getExecutor().getPort() : 0);
        executor.setLogPath(properties.getExecutor().getLogPath());
        executor.setLogRetentionDays(properties.getExecutor().getLogRetentionDays());
        return executor;
    }
}
