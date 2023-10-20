package com.matrix.jpush.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "jpush")
public class JPushProperties {
    private String masterSecret;
    private String appKey;
    private Boolean apnsProduction;
    private String privateKey;
    private String loginTokenUrl = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
}
