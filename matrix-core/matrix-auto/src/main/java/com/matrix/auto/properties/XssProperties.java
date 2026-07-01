package com.matrix.auto.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * XSS跨站脚本配置
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "matrix.xss")
public class XssProperties {
    /**
     * Xss开关
     */
    private Boolean enabled;

    /**
     * 排除路径
     */
    private List<String> excludeUrls = new ArrayList<>();
}
