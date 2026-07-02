package com.matrix.jpush.properties;

import lombok.Data;

/**
 * 极光推送配置属性
 */
@Data
public class JPushProperties {
    /**
     * 手机号验证接口地址
     */
    private final String loginTokenUrl = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
    /**
     * 应用 AppKey
     */
    private String appKey;
    /**
     * 应用 MasterSecret
     */
    private String masterSecret;
    /**
     * APNs 是否生产环境
     */
    private Boolean apnsProduction = true;
    /**
     * RSA 私钥，用于解密手机号
     */
    private String privateKey;
}
