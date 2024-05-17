package com.matrix.api.resource.client;

import com.matrix.common.constant.ServerNameConstants;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * OSS存储对象服务
 *
 * @author LeonZhou
 * @since 2023/6/21
 */
@FeignClient(contextId = "OssApi", value = ServerNameConstants.RESOURCE, path = OssApi.PREFIX)
public interface OssApi {

    String PREFIX = "/oss";


}
