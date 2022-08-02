package com.matrix.config;

import com.google.common.collect.Lists;
import com.matrix.properties.SwaggerProperties;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Set;

/**
 * 描述：swagger文档配置
 *
 * @author zwl
 * @since 2022/7/6 10:12
 **/
@EnableOpenApi
@EnableConfigurationProperties(SwaggerProperties.class)
public class MatrixSwaggerAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "matrix.swagger", name = "enable", havingValue = "true")
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.OAS_30)
                .enable(swaggerProperties.getEnable())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .protocols(Set.of("https", "http"))
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    /**
     * API 页面上半部分展示信息
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getName() + " Api Doc")
                .description(swaggerProperties.getDescription())
                .contact(new Contact("zwl", null, "zhaoweilong176@gmail.com"))
                .version("Application Version: " + swaggerProperties.getVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion())
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }


    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }


}
