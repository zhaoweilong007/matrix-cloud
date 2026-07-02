package com.matrix.jpush.config;

import com.matrix.jpush.JPushInitializer;
import com.matrix.jpush.properties.JPushProperties;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 极光推送自动配置类，加载多应用配置并注册初始化器
 **/
@AutoConfiguration
public class JPushAutoConfig {

    /**
     * 加载极光推送多应用配置
     *
     * @return 应用配置 Map，key 为应用标识
     */
    @Bean
    @ConfigurationProperties(prefix = "matrix.jpush")
    public Map<String, JPushProperties> jpushConfig() {
        return new LinkedHashMap<>();
    }

    /**
     * 注册极光推送初始化器
     *
     * @param jpushConfig 应用配置 Map
     * @return 初始化器实例
     */
    @Bean
    public JPushInitializer jPushInitializer(Map<String, JPushProperties> jpushConfig) {
        return new JPushInitializer(jpushConfig);
    }
}
