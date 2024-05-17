package com.matrix.jpush.properties;

import lombok.Data;


@Data
public class JPushProperties {
    private final String loginTokenUrl = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
    private String appKey;
    private String masterSecret;
    private Boolean apnsProduction = true;
    private String privateKey;
}
