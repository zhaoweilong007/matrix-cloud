package com.matrix.social.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 第三方登录配置属性。
 *
 * <p>支持多平台配置，示例：</p>
 * <pre>
 * matrix.social:
 *   platforms:
 *     gitee:
 *       client-id: xxx
 *       client-secret: xxx
 *       redirect-uri: http://example.com/callback/gitee
 * </pre>
 *
 * @author matrix
 */
@Data
@ConfigurationProperties(prefix = "matrix.social")
public class SocialProperties {

    /** 是否启用 */
    private boolean enabled = false;

    /** 平台配置：key=平台标识(gitee/github/wechat_open等), value=平台配置 */
    private Map<String, PlatformConfig> platforms = new HashMap<>();

    @Data
    public static class PlatformConfig {
        /** 客户端 ID */
        private String clientId;
        /** 客户端密钥 */
        private String clientSecret;
        /** 回调地址 */
        private String redirectUri;
        /** 是否忽略校验 state（默认 false） */
        private boolean ignoreCheckState = false;
    }
}
