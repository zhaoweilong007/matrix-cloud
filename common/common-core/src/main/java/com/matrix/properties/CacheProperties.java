package com.matrix.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 描述：cache配置
 *
 * @author zwl
 * @since 2022/7/7 15:02
 **/
@Data
@ConfigurationProperties(prefix = "matrix.cache")
public class CacheProperties {

    /**
     * cacheName
     */
    private String name;

    /**
     * 过期时间
     */
    private Duration ttl = Duration.ofSeconds(3600);


    private String prefix;


}
