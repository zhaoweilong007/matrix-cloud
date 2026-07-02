package com.matrix.api.resource.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OSS 对象存储服务 Feign 客户端。
 *
 * <p>提供文件上传、删除、预签名 URL 等对象存储操作。</p>
 */
@FeignClient(contextId = "OssApi", value = ServerNameConstants.RESOURCE, path = OssApi.PREFIX)
public interface OssApi {

    String PREFIX = "/oss";

    /**
     * 上传文件并返回访问 URL。
     *
     * @param objectName  对象名（存储路径）
     * @param contentType 文件内容类型
     * @return 文件访问 URL
     */
    @Operation(summary = "文件上传", description = "上传文件到对象存储并返回 URL")
    @PostMapping("/upload")
    R<String> upload(@RequestParam("objectName") String objectName,
                     @RequestParam("contentType") String contentType);

    /**
     * 删除文件。
     *
     * @param objectName 对象名
     * @return 是否删除成功
     */
    @Operation(summary = "文件删除", description = "从对象存储删除指定文件")
    @DeleteMapping("/delete")
    R<Boolean> delete(@RequestParam("objectName") String objectName);

    /**
     * 生成预签名 URL。
     *
     * @param objectName 对象名
     * @param expires    过期时间（秒），默认 3600
     * @return 预签名 URL
     */
    @Operation(summary = "预签名 URL", description = "生成临时访问的预签名 URL")
    @GetMapping("/presigned-url")
    R<String> getPresignedUrl(@RequestParam("objectName") String objectName,
                              @Parameter(description = "过期时间（秒）") @RequestParam(defaultValue = "3600") Integer expires);
}
