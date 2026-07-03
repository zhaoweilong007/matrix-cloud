package com.matrix.resource.controller;

import com.matrix.api.resource.client.OssApi;
import com.matrix.common.result.R;
import com.matrix.log.annotation.Log;
import com.matrix.log.enums.BusinessType;
import com.matrix.oss.core.OssClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @Log(title = "对象存储", businessType = BusinessType.INSERT)
    public R<String> upload(MultipartFile file, String objectName, String contentType) {
        try {
            String url = ossClient.upload(objectName, file.getInputStream(), contentType);
            return R.success(url);
        } catch (IOException e) {
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    @Log(title = "对象存储", businessType = BusinessType.DELETE)
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
