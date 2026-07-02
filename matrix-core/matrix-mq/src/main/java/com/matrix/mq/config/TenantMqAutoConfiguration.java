package com.matrix.mq.config;

import com.matrix.mq.producer.RocketMqTemplate;
import com.matrix.mq.tenant.TenantRocketMqTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * MQ 租户传播自动配置
 *
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.mq", value = "enabled", havingValue = "true")
public class TenantMqAutoConfiguration {

    /**
     * 注册支持租户传播的 RocketMQ 模板
     */
    @Bean
    @ConditionalOnBean(RocketMqTemplate.class)
    @ConditionalOnMissingBean(TenantRocketMqTemplate.class)
    public TenantRocketMqTemplate tenantRocketMqTemplate(RocketMqTemplate rocketMqTemplate) {
        return new TenantRocketMqTemplate(rocketMqTemplate);
    }
}
