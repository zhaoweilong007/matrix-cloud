package com.matrix.resource.controller;

import com.matrix.api.resource.client.OssApi;
import com.matrix.common.result.R;
import com.matrix.oss.core.OssClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对象存储服务控制器。
 *
 * <p>委托 {@link OssClient} 完成对象存储操作。</p>
 */
@RestController
@RequestMapping(value = OssApi.PREFIX)
@Tag(name = "对象存储服务", description = "对象存储服务")
@RequiredArgsConstructor
public class OssApiController implements OssApi {

    private final OssClient ossClient;

    @Override
    public R<String> upload(String objectName, String contentType) {
        // 从请求上下文获取文件流；简化实现使用空流，实际需配合 MultipartFile
        byte[] empty = new byte[0];
        String url = ossClient.upload(objectName, new ByteArrayInputStream(empty), contentType);
        return R.success(url);
    }

    @Override
    public R<Boolean> delete(String objectName) {
        ossClient.delete(objectName);
        return R.success(true);
    }

    @Override
    public R<String> getPresignedUrl(String objectName, Integer expires) {
        String url = ossClient.getPresignedUrl(objectName, Duration.ofSeconds(expires));
        return R.success(url);
    }
}
