package com.matrix.web.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.web.model.ApiAccessLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * API 访问日志 Feign 客户端，用于向 resource 服务发送访问日志
 */
@FeignClient(contextId = "ApiAccessLogApi", value = ServerNameConstants.RESOURCE, path = "/apiAccessLog")
public interface ApiAccessLogApi {

  /**
   * 发送 API 访问日志
   *
   * @param apiAccessLog 访问日志
   */
  @PostMapping("/createApiAccessLog")
  void createApiAccessLog(@RequestBody ApiAccessLog apiAccessLog);
}
