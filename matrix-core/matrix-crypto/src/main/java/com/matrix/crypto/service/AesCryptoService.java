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
     * 使用 ThreadLocal 线程安全包装 AES 加密器
     */
    private final ThreadLocal<AES> aesHolder;

    /**
     * 使用密钥构造 AES 加解密服务
     *
     * @param secretKey AES 密钥（16/24/32 字节）
     */
    public AesCryptoService(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.aesHolder = ThreadLocal.withInitial(() -> new AES(keyBytes));
    }

    @Override
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        try {
            return aesHolder.get().encryptBase64(data);
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
            return aesHolder.get().decryptStr(encryptedData);
        } catch (Exception e) {
            log.error("AES解密失败: {}", e.getMessage(), e);
            throw new RuntimeException("解密失败", e);
        }
    }
}
