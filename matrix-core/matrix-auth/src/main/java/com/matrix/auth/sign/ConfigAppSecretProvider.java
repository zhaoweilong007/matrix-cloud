package com.matrix.auth.sign;

import lombok.RequiredArgsConstructor;

/**
 * 基于配置文件的 AppSecret 提供者（默认实现）。
 * <p>从 {@code matrix.api-signature.secrets} 配置项读取 appId→secret 映射。
 * 支持 Nacos 动态刷新。</p>
 *
 * <p>若需改为从数据库查询，实现 {@link AppSecretProvider} 并注册为 Spring Bean 即可
 * 自动替换此默认实现。</p>
 */
@RequiredArgsConstructor
public class ConfigAppSecretProvider implements AppSecretProvider {

    private final ApiSignatureProperties properties;

    @Override
    public String getSecret(String appId) {
        return properties.getSecrets().get(appId);
    }
}
