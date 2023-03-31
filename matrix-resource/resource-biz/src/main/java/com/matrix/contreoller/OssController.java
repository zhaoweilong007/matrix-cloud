package com.matrix.contreoller;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.matrix.api.resource.OssAPI;
import com.matrix.constants.SysConstant;
import com.matrix.convert.ResourceConvert;
import com.matrix.entity.vo.Result;
import com.matrix.exception.BusinessErrorType;
import com.matrix.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 描述：oss控制器
 *
 * @author zwl
 * @since 2022/11/23 16:43
 **/
@RestController
@Slf4j
@RequestMapping(OssAPI.PREFIX)
public class OssController implements OssAPI {


  @Autowired
  private FileStorageService fileStorageService;

  /**
   * 上传文件，成功返回文件 url
   */
  @Override
  public Result<String> upload(MultipartFile file) {
    FileInfo fileInfo = fileStorageService.of(file)
        .setPath("upload/"+ ServletUtils.getHeader(SysConstant.APP_NAME))
        .upload();
    return fileInfo == null ? Result.fail(BusinessErrorType.UPLOAD_ERROR) : Result.success(fileInfo.getUrl());
  }

  /**
   * 上传图片，成功返回文件信息
   */
  @Override
  public Result<com.matrix.api.resource.model.FileInfo> uploadImage(MultipartFile file) {
    final FileInfo fileInfo = fileStorageService.of(file)
        .image(img -> img.size(1000, 1000))  //将图片大小调整到 1000*1000
        .thumbnail(th -> th.size(200, 200))  //再生成一张 200*200 的缩略图
        .upload();
    return Result.success(ResourceConvert.INSTANCE.convert(fileInfo));
  }

  /**
   * 上传文件到指定存储平台，成功返回文件信息
   */
  @Override
  public Result<com.matrix.api.resource.model.FileInfo> uploadPlatform(MultipartFile file,@PathVariable("platform")String platform) {
    final FileInfo fileInfo = fileStorageService.of(file)
        .setPlatform(platform)
        .upload();
    return Result.success(ResourceConvert.INSTANCE.convert(fileInfo));
  }

}
