package com.matrix.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 描述：matrix配置
 *
 * @author zwl
 * @since 2022/7/7 15:01
 **/
@ConfigurationProperties(prefix = "matrix")
@Data
public class MatrixProperties {

    private List<CacheProperties> cache;

}
