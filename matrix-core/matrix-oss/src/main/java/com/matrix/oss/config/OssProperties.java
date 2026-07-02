package com.matrix.oss.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OSS 对象存储配置属性。
 *
 * <p>S3 兼容协议，支持阿里云 OSS / MinIO / 腾讯云 COS 等。</p>
 *
 */
@Data
@ConfigurationProperties(prefix = "matrix.oss")
public class OssProperties {

    /** 是否启用 */
    private boolean enabled = false;

    /** OSS 端点地址（如 https://oss-cn-hangzhou.aliyuncs.com） */
    private String endpoint;

    /** 区域 */
    private String region = "cn-hangzhou";

    /** 访问密钥 ID */
    private String accessKey;

    /** 访问密钥 Secret */
    private String secretKey;

    /** Bucket 名称 */
    private String bucket;

    /** 自定义域名（CDN 加速域名），用于生成访问 URL */
    private String domain;

    /** 上传文件大小限制（默认 100MB） */
    private long maxUploadSize = 100 * 1024 * 1024;

    /** 预签名 URL 默认过期时间 */
    private Duration presignedExpiration = Duration.ofHours(1);
}
