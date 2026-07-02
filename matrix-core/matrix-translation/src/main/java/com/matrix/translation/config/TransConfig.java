package com.matrix.translation.config;

import org.dromara.cache.service.TransCacheManager;
import org.dromara.trans.ds.DataSourceSetter;
import org.dromara.trans.service.impl.SimpleTransService;
import com.matrix.translation.entity.SysUserTrans;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * easy-trans 自动配置类，初始化远程翻译缓存和空数据源处理
 */
@AutoConfiguration
@ConditionalOnBean(TransCacheManager.class)
public class TransConfig {

    @Autowired
    private TransCacheManager transCacheManager;

    /**
     * 初始化远程翻译缓存配置
     */
    @PostConstruct
    public void init() {
        transCacheManager.setRpcTransCache(
                SysUserTrans.CLASS_NAME,
                SimpleTransService.TransCacheSett.builder()
                        .cacheSeconds(60 * 60 * 24 * 7)
                        .maxCache(10000)
                        .build());
    }

    /**
     * 创建空数据源设置器，easy-trans 在多数据源场景下的默认实现
     *
     * @return DataSourceSetter 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSourceSetter dataSourceSetter() {
        return new DataSourceSetter() {
            @Override
            public void setDataSource(String s) {}

            @Override
            public Map<Object, Object> getContext() {
                return Map.of();
            }

            @Override
            public void setContext(Map<Object, Object> map) {}
        };
    }
}
