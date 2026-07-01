package com.matrix.oss.core;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * OSS 对象存储客户端统一接口。
 *
 * @author matrix
 */
public interface OssClient {

    /**
     * 上传文件。
     *
     * @param objectName  对象名（路径）
     * @param input       输入流
     * @param contentType 内容类型
     * @return 文件访问 URL
     */
    String upload(String objectName, InputStream input, String contentType);

    /**
     * 上传文件并设置元数据。
     *
     * @param objectName  对象名
     * @param input       输入流
     * @param contentType 内容类型
     * @param metadata    自定义元数据
     * @return 文件访问 URL
     */
    String upload(String objectName, InputStream input, String contentType, Map<String, String> metadata);

    /**
     * 下载文件。
     *
     * @param objectName 对象名
     * @return 文件输入流
     */
    InputStream download(String objectName);

    /**
     * 删除文件。
     *
     * @param objectName 对象名
     */
    void delete(String objectName);

    /**
     * 批量删除文件。
     *
     * @param objectNames 对象名列表
     */
    void deleteBatch(List<String> objectNames);

    /**
     * 生成预签名 URL（用于临时访问）。
     *
     * @param objectName 对象名
     * @param expiration 过期时间
     * @return 预签名 URL
     */
    String getPresignedUrl(String objectName, Duration expiration);

    /**
     * 列出指定前缀下的所有对象。
     *
     * @param prefix 前缀
     * @return 对象列表
     */
    List<S3Object> list(String prefix);

    /**
     * 检查对象是否存在。
     *
     * @param objectName 对象名
     * @return true 如果存在
     */
    boolean exists(String objectName);

    /**
     * 获取对象访问 URL。
     *
     * @param objectName 对象名
     * @return 访问 URL
     */
    String getObjectUrl(String objectName);

    /**
     * 获取 Bucket 名称。
     *
     * @return bucket
     */
    String getBucket();
}
