package com.matrix.social.core;

import com.matrix.social.config.SocialProperties;
import com.matrix.social.config.SocialProperties.PlatformConfig;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JustAuth 平台认证请求工厂。
 *
 * <p>按平台标识（gitee/github/wechat_open 等）创建 {@link AuthRequest}。</p>
 *
 */
public class SocialAuthFactory {

    private static final Logger log = LoggerFactory.getLogger(SocialAuthFactory.class);

    /**
     * 第三方登录配置
     */
    private final SocialProperties properties;
    /**
     * 已初始化的 AuthRequest 缓存
     */
    private final Map<String, AuthRequest> authRequestCache = new ConcurrentHashMap<>();

    public SocialAuthFactory(SocialProperties properties) {
        this.properties = properties;
        for (String platform : properties.getPlatforms().keySet()) {
            AuthRequest request = createAuthRequest(platform);
            if (request != null) {
                authRequestCache.put(platform, request);
            }
        }
    }

    /**
     * 获取指定平台的 AuthRequest。
     *
     * @param platform 平台标识（gitee/github/wechat_open 等）
     * @return AuthRequest，不存在返回 null
     */
    public AuthRequest getAuthRequest(String platform) {
        AuthRequest request = authRequestCache.get(platform);
        if (request == null) {
            throw new IllegalArgumentException("未配置的第三方平台: " + platform
                    + "，可用平台: " + authRequestCache.keySet());
        }
        return request;
    }

    /**
     * 已知平台标识到 AuthRequest 实现类的映射
     */
    private static final Map<String, Class<? extends AuthRequest>> PLATFORM_CLASSES = new LinkedHashMap<>();

    static {
        // 注册已知平台的 Request 实现
        register("gitee", "me.zhyd.oauth.request.AuthGiteeRequest");
        register("github", "me.zhyd.oauth.request.AuthGithubRequest");
        register("wechat_open", "me.zhyd.oauth.request.AuthWeChatOpenRequest");
        register("wechat_enterprise", "me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest");
        register("dingtalk", "me.zhyd.oauth.request.AuthDingTalkRequest");
        register("feishu", "me.zhyd.oauth.request.AuthFeishuRequest");
        register("alipay", "me.zhyd.oauth.request.AuthAlipayRequest");
        register("qq", "me.zhyd.oauth.request.AuthQqRequest");
        register("weibo", "me.zhyd.oauth.request.AuthWeiboRequest");
        register("baidu", "me.zhyd.oauth.request.AuthBaiduRequest");
        register("coding", "me.zhyd.oauth.request.AuthCodingRequest");
        register("oschina", "me.zhyd.oauth.request.AuthOschinaRequest");
        register("csdn", "me.zhyd.oauth.request.AuthCsdnRequest");
    }

    /**
     * 注册平台标识和对应的 AuthRequest 实现类
     *
     * @param platform  平台标识
     * @param className AuthRequest 实现类的全限定名
     */
    @SuppressWarnings("unchecked")
    private static void register(String platform, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (AuthRequest.class.isAssignableFrom(clazz)) {
                PLATFORM_CLASSES.put(platform, (Class<? extends AuthRequest>) clazz);
            }
        } catch (ClassNotFoundException ignored) { }
    }

    /**
     * 根据平台配置创建 AuthRequest 实例
     *
     * @param platform 平台标识
     * @return AuthRequest 实例，配置不存在或创建失败时返回 null
     */
    private AuthRequest createAuthRequest(String platform) {
        PlatformConfig config = properties.getPlatforms().get(platform);
        if (config == null) {
            return null;
        }
        Class<? extends AuthRequest> requestClass = PLATFORM_CLASSES.get(platform);
        if (requestClass == null) {
            log.warn("Unknown social platform: {}. Supported: {}", platform, PLATFORM_CLASSES.keySet());
            return null;
        }
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .redirectUri(config.getRedirectUri())
                .ignoreCheckState(config.isIgnoreCheckState())
                .build();
        try {
            Constructor<? extends AuthRequest> constructor = requestClass.getConstructor(AuthConfig.class);
            AuthRequest request = constructor.newInstance(authConfig);
            log.info("Registered social platform: {} ({})", platform, requestClass.getSimpleName());
            return request;
        } catch (Exception e) {
            log.error("Failed to create AuthRequest for platform: {}", platform, e);
            return null;
        }
    }
}
