package com.matrix.oss.core;

import com.matrix.oss.config.OssProperties;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * S3 兼容协议 OSS 客户端实现。
 *
 * <p>基于 AWS SDK for Java v2，兼容阿里云 OSS / MinIO / 腾讯云 COS 等。</p>
 *
 */
public class OssClientImpl implements OssClient {

    private static final Logger log = LoggerFactory.getLogger(OssClientImpl.class);

    /** AWS S3 客户端 */
    private final S3Client s3Client;
    /** 预签名 URL 生成器 */
    private final S3Presigner presigner;
    /** OSS 配置属性 */
    private final OssProperties properties;

    /**
     * 构造 OSS 客户端实现。
     *
     * @param properties OSS 配置属性（端点、凭证、Bucket 等）
     */
    public OssClientImpl(OssProperties properties) {
        this.properties = properties;
        this.s3Client = S3Client.builder()
                .region(Region.of(properties.getRegion()))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())))
                .build();
        this.presigner = S3Presigner.builder()
                .region(Region.of(properties.getRegion()))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())))
                .build();
        log.info("OSS client initialized: endpoint={}, bucket={}", properties.getEndpoint(), properties.getBucket());
    }

    @Override
    public String upload(String objectName, InputStream input, String contentType) {
        return upload(objectName, input, contentType, null);
    }

    @Override
    public String upload(String objectName, InputStream input, String contentType, Map<String, String> metadata) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(objectName)
                .contentType(contentType);
        if (metadata != null && !metadata.isEmpty()) {
            builder.metadata(metadata);
        }
        try {
            s3Client.putObject(builder.build(), RequestBody.fromInputStream(input, input.available()));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to upload file: " + objectName, e);
        }
        log.debug("File uploaded: {}", objectName);
        return getObjectUrl(objectName);
    }

    @Override
    public InputStream download(String objectName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(objectName)
                .build();
        return s3Client.getObject(request);
    }

    @Override
    public void delete(String objectName) {
        s3Client.deleteObject(b -> b.bucket(properties.getBucket()).key(objectName));
        log.debug("File deleted: {}", objectName);
    }

    @Override
    public void deleteBatch(List<String> objectNames) {
        List<ObjectIdentifier> identifiers = objectNames.stream()
                .map(name -> ObjectIdentifier.builder().key(name).build())
                .toList();
        s3Client.deleteObjects(DeleteObjectsRequest.builder()
                .bucket(properties.getBucket())
                .delete(d -> d.objects(identifiers))
                .build());
        log.debug("Batch deleted {} files", objectNames.size());
    }

    @Override
    public String getPresignedUrl(String objectName, Duration expiration) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiration != null ? expiration : properties.getPresignedExpiration())
                .getObjectRequest(r -> r.bucket(properties.getBucket()).key(objectName))
                .build();
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    @Override
    public List<S3Object> list(String prefix) {
        List<S3Object> result = new ArrayList<>();
        String continuationToken = null;
        do {
            ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder()
                    .bucket(properties.getBucket())
                    .prefix(prefix)
                    .maxKeys(1000);
            if (continuationToken != null) {
                builder.continuationToken(continuationToken);
            }
            ListObjectsV2Response response = s3Client.listObjectsV2(builder.build());
            result.addAll(response.contents());
            continuationToken = response.isTruncated() ? response.nextContinuationToken() : null;
        } while (continuationToken != null);
        return result;
    }

    @Override
    public boolean exists(String objectName) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(objectName)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public String getObjectUrl(String objectName) {
        String host = properties.getDomain() != null && !properties.getDomain().isBlank()
                ? properties.getDomain()
                : properties.getEndpoint() + "/" + properties.getBucket();
        return host + "/" + objectName;
    }

    @Override
    public String getBucket() {
        return properties.getBucket();
    }
}
