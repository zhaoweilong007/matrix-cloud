package com.matrix.crypto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加解密配置
 *
 * @author matrix
 */
@Data
@ConfigurationProperties(prefix = "matrix.crypto")
public class CryptoProperties {

    /**
     * 是否启用加解密
     */
    private boolean enabled = false;

    /**
     * 加解密类型：AES / RSA
     */
    private CryptoType type = CryptoType.AES;

    /**
     * AES 密钥（16/24/32字节）
     */
    private String secretKey;

    /**
     * RSA 公钥
     */
    private String publicKey;

    /**
     * RSA 私钥
     */
    private String privateKey;

    /**
     * 请求体加密字段名
     */
    private String encryptField = "encryptData";

    public enum CryptoType {
        AES, RSA
    }
}
