package com.matrix.crypto.service;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import java.nio.charset.StandardCharsets;

/**
 * 国密 SM2 非对称加密服务（椭圆曲线公钥密码算法）。
 *
 * <p>SM2 是中国国家密码管理局发布的非对称加密算法标准，
 * 安全强度等同于 RSA 3072 位。私钥长度 64 字节，公钥长度 128 字节（十六进制）。</p>
 *
 */
public class Sm2CryptoService implements CryptoService {

    /**
     * 使用 ThreadLocal 线程安全包装 SM2 加密器
     */
    private final ThreadLocal<SM2> sm2Holder;

    /**
     * 使用私钥和公钥构造 SM2 加解密服务
     *
     * @param privateKey SM2 私钥（64字节十六进制）
     * @param publicKey  SM2 公钥（128字节十六进制）
     */
    public Sm2CryptoService(String privateKey, String publicKey) {
        if (privateKey == null || publicKey == null) {
            throw new IllegalArgumentException("SM2 公私钥不能为空");
        }
        // Hutool 支持十六进制字符串或 Base64 格式的密钥
        this.sm2Holder = ThreadLocal.withInitial(() -> SmUtil.sm2(privateKey, publicKey));
    }

    @Override
    public String encrypt(String data) {
        return sm2Holder.get().encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    @Override
    public String decrypt(String encryptedData) {
        return sm2Holder.get().decryptStr(encryptedData, KeyType.PrivateKey, StandardCharsets.UTF_8);
    }
}
