package com.matrix.crypto.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * RSA 加解密服务
 *
 */
@Slf4j
public class RsaCryptoService implements CryptoService {

    /**
     * RSA 加密器（Hutool 封装）
     */
    private final RSA rsa;

    /**
     * 使用公钥和私钥构造 RSA 加解密服务
     *
     * @param publicKey  RSA 公钥
     * @param privateKey RSA 私钥
     */
    public RsaCryptoService(String publicKey, String privateKey) {
        this.rsa = new RSA(privateKey, publicKey);
    }

    @Override
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        try {
            return rsa.encryptBase64(data.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        } catch (Exception e) {
            log.error("RSA加密失败: {}", e.getMessage(), e);
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("RSA解密失败: {}", e.getMessage(), e);
            throw new RuntimeException("解密失败", e);
        }
    }
}
