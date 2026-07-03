package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 对象存储 OSS 配置属性
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "matrix.oss.translation")
@Component
public class OssProperties {

    /**
     * OSS 服务端点 URL
     */
    private String endpoint;

    /**
     * OSS 访问密钥 ID
     */
    private String accessKeyId;

    /**
     * OSS 访问密钥密码
     */
    private String accessKeySecret;

    /**
     * 默认存储桶名称
     */
    private String bucketName;

    /**
     * 对象键前缀
     */
    private String prefix;
}
