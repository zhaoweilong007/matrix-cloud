package com.matrix.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 描述：权限配置
 *
 * @author zwl
 * @since 2022/7/8 15:03
 **/
@ConfigurationProperties(prefix = "matrix.security")
@Configuration(proxyBeanMethods = false)
@Data
public class MatrixSecurityProperties {

    /**
     * 放行白名单
     */
    List<String> whiteUrls = Lists.newArrayList();


}
