package com.matrix.crypto.service;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import java.nio.charset.StandardCharsets;

/**
 * 国密 SM4 对称加密服务。
 *
 * <p>SM4 是中国国家密码管理局发布的对称加密算法标准，
 * 密钥长度 16 字节（128 位），分组长度 16 字节。</p>
 *
 * @author matrix
 */
public class Sm4CryptoService implements CryptoService {

    private final SymmetricCrypto sm4;

    public Sm4CryptoService(String secretKey) {
        if (secretKey == null || secretKey.length() != 16) {
            throw new IllegalArgumentException("SM4 密钥长度必须为16字节");
        }
        this.sm4 = SmUtil.sm4(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encrypt(String data) {
        return sm4.encryptBase64(data);
    }

    @Override
    public String decrypt(String encryptedData) {
        return sm4.decryptStr(encryptedData);
    }
}
