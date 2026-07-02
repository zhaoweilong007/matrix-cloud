package com.matrix.mybatis.metrics;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于Prometheus监控平台的HikariDataSource监控
 *
 */
@Configuration
@ConditionalOnBean(HikariDataSource.class)
@ConditionalOnClass({HikariDataSource.class, MeterRegistry.class})
public class HikaricpMetricsConfiguration {

    /**
     * 创建 HikariCP Prometheus 指标跟踪器工厂。
     *
     * @return MetricsTrackerFactory 实例
     */
    @Bean
    @ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
    public MetricsTrackerFactory druidFilterRegistrationBean() {
        return new PrometheusMetricsTrackerFactory();
    }
}
