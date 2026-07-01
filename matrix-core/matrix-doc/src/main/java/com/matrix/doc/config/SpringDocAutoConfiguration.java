package com.matrix.doc.config;

import com.matrix.doc.core.SaTokenSecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * SpringDoc OpenAPI 自动配置。
 *
 * <p>启用后自动提供 Swagger UI（/swagger-ui.html）和 OpenAPI JSON（/v3/api-docs）。</p>
 *
 * @author matrix
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springdoc.core.models.GroupedOpenApi")
@EnableConfigurationProperties(SpringDocProperties.class)
@ConditionalOnProperty(prefix = "matrix.doc", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SpringDocAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI(SpringDocProperties properties) {
        log.info("Initializing SpringDoc OpenAPI: title={}, version={}", properties.getTitle(), properties.getVersion());
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .description(properties.getDescription())
                        .version(properties.getVersion())
                        .contact(new Contact().name("Matrix Cloud").email("admin@matrix.cloud"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupedOpenApi defaultGroupedOpenApi(SpringDocProperties properties) {
        return GroupedOpenApi.builder()
                .group(properties.getGroupName())
                .pathsToMatch(properties.getPathsToMatch())
                .pathsToExclude(properties.getPathsToExclude())
                .build();
    }

    /**
     * Sa-Token 安全方案（仅在 Sa-Token 可用时注册）。
     * 通过 @ConditionalOnClass 避免硬依赖。
     */
    @Bean
    @ConditionalOnClass(name = "cn.dev33.satoken.stp.StpUtil")
    @ConditionalOnMissingBean
    public GlobalOpenApiCustomizer saTokenSecurityScheme() {
        log.info("Sa-Token detected, enabling Bearer Token auth in Swagger UI");
        return new SaTokenSecurityScheme();
    }
}
