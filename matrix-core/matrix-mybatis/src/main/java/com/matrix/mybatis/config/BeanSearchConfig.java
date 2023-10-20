package com.matrix.mybatis.config;

import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.SqlExecutor;
import cn.zhxu.bs.boot.BeanSearcherProperties;
import cn.zhxu.bs.implement.DefaultDbMapping;
import cn.zhxu.bs.implement.DefaultSqlExecutor;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.mybatis.convert.JsonFieldConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Consumer;

/**
 * 兼容mybatis plus注解
 *
 * @author ZhaoWeiLong
 * @since 2023/5/12
 **/
@AutoConfiguration
@Slf4j
public class BeanSearchConfig {


    /**
     * 适配dynamicDataSource 多数据源
     *
     * @param dataSource      数据源
     * @param dataSourceRoute 数据源路由
     * @param slowListener    慢 SQL 监听器
     * @param config          配置
     */
    @Bean
    @Primary
    public SqlExecutor sqlExecutor(ObjectProvider<DataSource> dataSource,
                                   ObjectProvider<DynamicRoutingDataSource> dataSourceRoute,
                                   ObjectProvider<SqlExecutor.SlowListener> slowListener,
                                   BeanSearcherProperties config) {
        DefaultSqlExecutor executor = new DefaultSqlExecutor(dataSource.getIfAvailable());
        ifAvailable(dataSourceRoute, route -> {
            route.getDataSources().forEach(executor::setDataSource);

        });
        ifAvailable(slowListener, executor::setSlowListener);
        executor.setSlowSqlThreshold(config.getSql().getSlowSqlThreshold());
        return executor;
    }

    private <T> void ifAvailable(ObjectProvider<T> provider, Consumer<T> consumer) {
        // 为了兼容 1.x 的 SpringBoot，最低兼容到 v1.4
        // 不直接使用 ObjectProvider.ifAvailable 方法
        T dependency = provider.getIfAvailable();
        if (dependency != null) {
            consumer.accept(dependency);
        }
    }

    @Bean
    public JsonFieldConvert jsonFieldConvert() {
        return new JsonFieldConvert();
    }


    @Bean
    @Primary
    public DefaultDbMapping bsJpaDbMapping(BeanSearcherProperties config) {
        DefaultDbMapping mapping = new DefaultDbMapping() {

            @Override
            public String toTableName(Class<?> beanClass) {
                final TableName table = beanClass.getAnnotation(TableName.class);
                if (table != null && StrUtil.isNotBlank(table.value())) {
                    return table.value();
                }
                return super.toTableName(beanClass);
            }

            @Override
            public String toColumnName(BeanField field) {
                TableField column = field.getAnnotation(TableField.class);
                if (column != null && StrUtil.isNotBlank(column.value())) {
                    return column.value();
                }
                return super.toColumnName(field);
            }

        };
        BeanSearcherProperties.Sql.DefaultMapping conf = config.getSql().getDefaultMapping();
        mapping.setTablePrefix(conf.getTablePrefix());
        mapping.setUpperCase(conf.isUpperCase());
        mapping.setUnderlineCase(conf.isUnderlineCase());
        mapping.setRedundantSuffixes(conf.getRedundantSuffixes());
        mapping.setIgnoreFields(conf.getIgnoreFields());
        mapping.setDefaultInheritType(conf.getInheritType());
        mapping.setDefaultSortType(conf.getSortType());
        return mapping;
    }


    @Bean
    public SqlExecutor.SlowListener slowSqlListener() {
        return (
                Class<?> beanClass,     // 发生慢 SQL 的实体类
                String slowSql,         // 慢 SQL 字符串
                List<Object> params,    // SQL 执行参数
                long timeCost           // 执行耗时（单位：ms）
        ) -> {
            // TODO: 监听处理
            log.warn("慢sql监听 beanClass=>{} timeCost=>{} sql=>{}", beanClass.getName(), timeCost, slowSql);
        };
    }

}
