package com.matrix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/8 15:03
 **/
@ConfigurationProperties(prefix = "matrix.security")
@Configuration
@Data
public class MatrixSecurityProperties {

    /**
     * 放行白名单
     */
    List<String> whiteUrls;

    /**
     * 拦截黑名单
     */
    List<String> blackUrls;


}
