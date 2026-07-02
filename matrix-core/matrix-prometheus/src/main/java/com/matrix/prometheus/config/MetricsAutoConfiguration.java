package com.matrix.prometheus.config;

import com.matrix.prometheus.endpoint.FeignClientEndpoint;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Micrometer 指标自动配置。
 *
 * <p>为应用注册全局通用标签（如 application name），
 * 并暴露 Feign 客户端端点供 Actuator 查看。</p>
 */
@AutoConfiguration
@ConditionalOnClass({MeterRegistryCustomizer.class})
@ConditionalOnProperty(prefix = "matrix.monitor", value = "metricsEnable", matchIfMissing = true)
public class MetricsAutoConfiguration {

    /**
     * 注册全局 Micrometer 通用标签，绑定应用名称。
     *
     * @param applicationName 应用名称
     * @return MeterRegistry 定制器
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    /**
     * 创建 Feign 客户端端点，用于 Actuator 查看当前注册的 Feign 客户端信息。
     *
     * @param context Spring 应用上下文
     * @return Feign 客户端端点
     */
    @Bean
    @ConditionalOnMissingBean
    public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
        return new FeignClientEndpoint(context);
    }
}
