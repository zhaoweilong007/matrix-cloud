package com.matrix.properties;

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
     * 缓存名称
     */
    private String name;

    /**
     * 过期时间
     */
    private Duration ttl;

    /**
     * 缓存key前缀
     */
    private String prefix;


}
