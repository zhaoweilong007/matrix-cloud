package com.matrix.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:34
 **/
@Data
@ConfigurationProperties(prefix = "matrix.tenant")
public class TenantProperties {

    public static final String TENANT_KEY = "tenant_id";

    private Boolean enable = true;

    private List<String> ignoreTables;

}
