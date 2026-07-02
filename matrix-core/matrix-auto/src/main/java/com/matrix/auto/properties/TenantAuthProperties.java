package com.matrix.auto.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 租户认证配置
 *
 **/
@RefreshScope
@ConfigurationProperties(prefix = "matrix.security.tenant")
@Data
public class TenantAuthProperties {

    /**
     * 校验是否认证地址
     */
    private List<String> authUrl = new ArrayList<>();
}
