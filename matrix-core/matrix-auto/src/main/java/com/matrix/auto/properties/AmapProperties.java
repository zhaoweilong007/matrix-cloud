package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 高德API 属性参数配置
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "amap.service")
@Component
public class AmapProperties {

    private String url;
    private String appkey;
}
