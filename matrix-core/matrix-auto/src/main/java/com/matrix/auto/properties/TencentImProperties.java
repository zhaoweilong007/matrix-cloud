package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/19
 **/
@ConfigurationProperties(prefix = "tencent.im")
@RefreshScope
@Component
@Data
public class TencentImProperties {

    private Long sdkappid;

    private String key;

    private String adminAcount;

    private String serviceImId;

}
