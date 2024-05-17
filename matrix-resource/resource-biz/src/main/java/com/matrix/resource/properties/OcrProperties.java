package com.matrix.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@ConfigurationProperties(prefix = "ocr")
@Data
public class OcrProperties {

    private Boolean enabled;
    private String secretId;
    private String secretKey;
}
