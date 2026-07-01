package com.matrix.oss.config;

import com.matrix.oss.core.OssClient;
import com.matrix.oss.core.OssClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * OSS 对象存储自动配置。
 *
 * @author matrix
 */
@AutoConfiguration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "matrix.oss", name = "enabled", havingValue = "true")
public class OssAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OssAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public OssClient ossClient(OssProperties properties) {
        log.info("OSS module initialized: endpoint={}, bucket={}", properties.getEndpoint(), properties.getBucket());
        return new OssClientImpl(properties);
    }
}
