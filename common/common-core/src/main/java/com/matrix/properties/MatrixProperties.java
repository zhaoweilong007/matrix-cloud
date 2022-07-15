package com.matrix.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 描述：matrix配置
 *
 * @author zwl
 * @since 2022/7/7 15:01
 **/
@ConfigurationProperties(prefix = "matrix")
@Configuration(proxyBeanMethods = false)
@Data
public class MatrixProperties {


    private SwaggerProperties swagger;

    private List<CacheProperties> cache;

    private TenantProperties tenant;
}
