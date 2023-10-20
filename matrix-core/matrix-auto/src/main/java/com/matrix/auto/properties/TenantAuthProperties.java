package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户认证配置
 *
 * @author ZhaoWeiLong
 * @since 2023/8/9
 **/
@RefreshScope
@ConfigurationProperties(prefix = "security.tenant")
@Data
public class TenantAuthProperties {

    /**
     * 校验是否认证地址
     */
    private List<String> authUrl = new ArrayList<>();
}
