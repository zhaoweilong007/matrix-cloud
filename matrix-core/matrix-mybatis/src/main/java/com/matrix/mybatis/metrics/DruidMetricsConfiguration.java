package com.matrix.mybatis.metrics;

import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Druid 连接池 Micrometer 指标自动配置。
 *
 * <p>当 classpath 存在 DruidDataSource 和 MeterRegistry 时，
 * 自动将所有 Druid 数据源的运行指标注册到 Micrometer。</p>
 */
@AutoConfiguration
@ConditionalOnClass({DruidDataSource.class, MeterRegistry.class})
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(prefix = "matrix.monitor", name = "datasource-metrics-enabled", matchIfMissing = true)
public class DruidMetricsConfiguration {

    /** Micrometer 指标注册表 */
    private final MeterRegistry registry;

    public DruidMetricsConfiguration(MeterRegistry registry) {
        this.registry = registry;
    }

    /**
     * 将所有 DruidDataSource 绑定到 Micrometer 指标注册表。
     *
     * @param dataSources 所有已注册的数据源
     */
    @Autowired
    public void bindMetricsRegistryToDruidDataSources(Collection<DataSource> dataSources) throws SQLException {
        List<DruidDataSource> druidDataSources = new ArrayList<>(dataSources.size());
        for (DataSource dataSource : dataSources) {
            if (dataSource instanceof DruidDataSource) {
                DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
                if (druidDataSource != null) {
                    druidDataSources.add(druidDataSource);
                }
            }
        }
        DruidCollector druidCollector = new DruidCollector(druidDataSources, registry);
        druidCollector.register();
    }
}
