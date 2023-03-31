package com.matrix.api.resource;

import com.matrix.api.resource.model.FileInfo;
import com.matrix.entity.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:49
 **/
@FeignClient(value = "resource", path = OssAPI.PREFIX)
@Api(tags = "储存服务")
public interface OssAPI {

    String PREFIX = "/oss";


    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    Result<String> upload(MultipartFile file);


    /**
     * 上传图片，成功返回文件信息
     */
    @PostMapping("/upload-image")
    @ApiOperation("上传图片")
    Result<FileInfo> uploadImage(MultipartFile file);


    /**
     * 上传文件到指定存储平台，成功返回文件信息
     */
    @PostMapping("/upload/{platform}")
    @ApiOperation("上传图片至指定平台")
    Result<FileInfo> uploadPlatform(MultipartFile file, @PathVariable("platform") String platform);
}
