package com.matrix.api.resource;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;

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
}
