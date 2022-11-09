package com.matrix.config;

import com.matrix.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/6/20 15:26
 **/
@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enable", havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
public class XxlJobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
        log.info(">>>>>>>>>>>>>>>>>> xxl-job config init.");
        log.info(">>>>>>>>>>>>>>>>>> xxl-job register address:{}", xxlJobProperties.getAdminAddresses());
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutorAppName());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutorAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutorIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutorPort());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutorLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutorLogRetentionDays());
        return xxlJobSpringExecutor;
    }


}
