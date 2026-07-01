package com.matrix.jpush.config;

import com.matrix.jpush.JPushInitializer;
import com.matrix.jpush.properties.JPushProperties;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ZhaoWeiLong
 * @since 2024/1/3
 **/
@AutoConfiguration
public class JPushAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "matrix.jpush")
    public Map<String, JPushProperties> jpushConfig() {
        return new LinkedHashMap<>();
    }

    @Bean
    public JPushInitializer jPushInitializer(Map<String, JPushProperties> jpushConfig) {
        return new JPushInitializer(jpushConfig);
    }
}
