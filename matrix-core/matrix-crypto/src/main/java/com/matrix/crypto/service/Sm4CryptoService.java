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
 */
public class Sm4CryptoService implements CryptoService {

    /**
     * 使用 ThreadLocal 线程安全包装 SM4 加密器
     */
    private final ThreadLocal<SymmetricCrypto> sm4Holder;

    /**
     * 使用密钥构造 SM4 加解密服务
     *
     * @param secretKey SM4 密钥（16 字节）
     */
    public Sm4CryptoService(String secretKey) {
        if (secretKey == null || secretKey.length() != 16) {
            throw new IllegalArgumentException("SM4 密钥长度必须为16字节");
        }
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.sm4Holder = ThreadLocal.withInitial(() -> SmUtil.sm4(keyBytes));
    }

    @Override
    public String encrypt(String data) {
        return sm4Holder.get().encryptBase64(data);
    }

    @Override
    public String decrypt(String encryptedData) {
        return sm4Holder.get().decryptStr(encryptedData);
    }
}
