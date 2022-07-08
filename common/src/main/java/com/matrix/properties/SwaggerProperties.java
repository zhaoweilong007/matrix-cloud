package com.matrix.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 描述：swagger配置
 *
 * @author zwl
 * @since 2022/7/6 15:22
 **/
@Data
public class SwaggerProperties {

    /**
     * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
     */
    private Boolean enable = true;

    /**
     * 项目应用名
     */
    @Value("${spring.application.name}")
    private String name;

    /**
     * 项目版本信息
     */
    private String version;

    /**
     * 项目描述信息
     */
    private String description;
}
