package com.matrix.auth.sign;

/**
 * API 签名 AppSecret 提供者 SPI。
 * <p>默认实现 {@link ConfigAppSecretProvider} 从 Nacos 配置文件读取。
 * 业务方可实现此接口并注册为 Spring Bean 以替换默认实现（如从数据库动态查询）。</p>
 */
public interface AppSecretProvider {

    /**
     * 根据 appId 获取对应的 appSecret。
     *
     * @param appId 应用ID
     * @return appSecret，找不到时返回 null
     */
    String getSecret(String appId);
}
