package com.matrix.config;

import lombok.Data;

import java.time.Duration;

/**
 * 描述：cache配置
 *
 * @author zwl
 * @since 2022/7/7 15:02
 **/
@Data
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
