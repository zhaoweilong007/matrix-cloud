package com.matrix.config;


import com.matrix.properties.*;
import org.springframework.context.annotation.Import;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/8 16:22
 **/
@Import({
        MatrixProperties.class,
        SecurityProperties.class,
        SwaggerProperties.class,
        TenantProperties.class,
        CacheProperties.class})
public class MatrixAutoConfiguration {


}
