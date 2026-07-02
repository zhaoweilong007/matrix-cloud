package com.matrix.auth.sign;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * API 签名配置属性。
 *
 * <p>配置前缀 {@code matrix.api-signature}，支持 Nacos 动态刷新。
 * {@code secrets} 映射 {@code appId → appSecret}。</p>
 */
@Data
@ConfigurationProperties(prefix = "matrix.api-signature")
public class ApiSignatureProperties {

    /**
     * appId 到 appSecret 的映射，从 Nacos 配置中心动态加载
     */
    private Map<String, String> secrets = new HashMap<>();
}
