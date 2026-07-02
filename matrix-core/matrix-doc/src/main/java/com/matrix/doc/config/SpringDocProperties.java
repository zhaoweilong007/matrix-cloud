package com.matrix.doc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringDoc API 文档配置属性。
 *
 */
@Data
@ConfigurationProperties(prefix = "matrix.doc")
public class SpringDocProperties {

    /** 是否启用 API 文档 */
    private boolean enabled = true;

    /** API 标题 */
    private String title = "Matrix Cloud API";

    /** API 描述 */
    private String description = "Matrix Cloud 微服务 API 文档";

    /** API 版本 */
    private String version = "1.0.0";

    /** API 分组名称 */
    private String groupName = "default";

    /** 匹配路径 */
    private String[] pathsToMatch = {"/**"};

    /** 排除路径 */
    private String[] pathsToExclude = {"/error", "/actuator/**"};
}
