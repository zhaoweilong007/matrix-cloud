package com.matrix.social.config;

import com.matrix.social.core.SocialAuthFactory;
import com.matrix.social.core.SocialAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 第三方登录自动配置。
 *
 * @author matrix
 */
@AutoConfiguration
@EnableConfigurationProperties(SocialProperties.class)
@ConditionalOnProperty(prefix = "matrix.social", name = "enabled", havingValue = "true")
public class SocialAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SocialAutoConfiguration.class);

    @Bean
    public SocialAuthFactory socialAuthFactory(SocialProperties properties) {
        log.info("Social login module initialized with {} platform(s): {}",
                properties.getPlatforms().size(), properties.getPlatforms().keySet());
        return new SocialAuthFactory(properties);
    }

    @Bean
    public SocialAuthService socialAuthService(SocialAuthFactory authFactory) {
        return new SocialAuthService(authFactory);
    }
}
