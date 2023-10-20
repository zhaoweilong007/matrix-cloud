package com.matrix.sensitive.config;

import com.matrix.auto.properties.YiDunProperties;
import com.matrix.sensitive.listener.SensitiveCheckEventListener;
import com.matrix.sensitive.utils.SensitiveUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/23
 **/
@AutoConfiguration
@EnableConfigurationProperties(YiDunProperties.class)
public class SensitiveConfig {


    @Bean
    public SensitiveUtils sensitiveWordUtils(YiDunProperties yiDunProperties) {
        return new SensitiveUtils(yiDunProperties);
    }

    @Bean
    public SensitiveCheckEventListener sensitiveCheckEventListener() {
        return new SensitiveCheckEventListener();
    }


}
