package com.matrix.crypto.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * RSA 加解密服务
 *
 * @author matrix
 */
@Slf4j
public class RsaCryptoService implements CryptoService {

    private final RSA rsa;

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
