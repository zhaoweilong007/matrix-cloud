package com.matrix.translation.config;

import com.fhs.cache.service.TransCacheManager;
import com.fhs.trans.ds.DataSourceSetter;
import com.fhs.trans.service.impl.SimpleTransService;
import com.matrix.translation.entity.SysUserTrans;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * easy-trans配置
 *
 * @author ZhaoWeiLong
 * @since 2023/8/30
 **/
@AutoConfiguration
public class TransConfig {

    @Autowired
    private TransCacheManager transCacheManager;

    @PostConstruct
    public void init() {
        transCacheManager.setRpcTransCache(SysUserTrans.CLASS_NAME,
                SimpleTransService.TransCacheSett.builder().cacheSeconds(60 * 60 * 24 * 7).maxCache(10000).build());
    }


    @Bean
    @ConditionalOnMissingBean
    public DataSourceSetter dataSourceSetter() {
        return datasourceName -> {
        };
    }
}
