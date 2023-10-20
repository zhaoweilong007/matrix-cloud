package com.matrix.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Deprecated
public class SmsProperties {
    private String accessKey;
    private String accessKeySecret;
}
