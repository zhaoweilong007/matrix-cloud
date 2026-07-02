package com.matrix.crypto.service;

/**
 * 加解密服务接口
 *
 */
public interface CryptoService {

    /**
     * 加密
     *
     * @param data 原始数据
     * @return 加密后的数据（Base64编码）
     */
    String encrypt(String data);

    /**
     * 解密
     *
     * @param encryptedData 加密数据（Base64编码）
     * @return 解密后的原始数据
     */
    String decrypt(String encryptedData);
}
