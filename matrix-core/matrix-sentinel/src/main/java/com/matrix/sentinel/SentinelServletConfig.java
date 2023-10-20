package com.matrix.sentinel;

import com.matrix.auto.factory.YamlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/17
 **/
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:sentinel.yml")
@Slf4j
public class SentinelServletConfig {


    @Bean
    public IpParse ipParse() {
        return new IpParse();
    }

}
