package com.matrix.api.resource;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:50
 **/
@FeignClient(value = "resource", path = EmailAPI.PREFIX)
@Api(tags = "邮箱服务")
public interface EmailAPI {
    String PREFIX = "/email";
}
