package com.matrix.api.resource;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:50
 **/
@FeignClient(value = "resource", path = SmsAPI.PREFIX)
@Api(tags = "短信服务")
public interface SmsAPI {
    String PREFIX = "/sms";
}
