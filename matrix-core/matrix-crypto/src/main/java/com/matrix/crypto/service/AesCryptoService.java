package com.matrix.crypto.service;

import cn.hutool.crypto.symmetric.AES;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * AES 加解密服务
 *
 */
@Slf4j
public class AesCryptoService implements CryptoService {

    /**
     * AES 加密器（Hutool 封装）
     */
    private final AES aes;

    /**
     * 使用密钥构造 AES 加解密服务
     *
     * @param secretKey AES 密钥（16/24/32 字节）
     */
    public AesCryptoService(String secretKey) {
        this.aes = new AES(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        try {
            return aes.encryptBase64(data);
        } catch (Exception e) {
            log.error("AES加密失败: {}", e.getMessage(), e);
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            return aes.decryptStr(encryptedData);
        } catch (Exception e) {
            log.error("AES解密失败: {}", e.getMessage(), e);
            throw new RuntimeException("解密失败", e);
        }
    }
}
