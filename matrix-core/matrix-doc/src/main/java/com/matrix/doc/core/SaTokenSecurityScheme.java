package com.matrix.doc.core;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.http.HttpHeaders;

/**
 * Sa-Token 安全方案定制器，为 Swagger UI 注入 Bearer Token 认证。
 *
 * <p>通过全局 OpenAPI 定制，给所有接口添加 SecurityScheme，
 * 用户在 Swagger UI 中输入 Sa-Token 即可测试需要认证的接口。</p>
 *
 * @author matrix
 */
public class SaTokenSecurityScheme implements GlobalOpenApiCustomizer {

    private static final String SECURITY_SCHEME_NAME = "Sa-Token";

    @Override
    public void customise(OpenAPI openApi) {
        // 添加 SecurityScheme 定义
        openApi.getComponents()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(HttpHeaders.AUTHORIZATION)
                                .description("Sa-Token 认证令牌，格式：Bearer {token}"));

        // 全局应用 SecurityRequirement
        openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
