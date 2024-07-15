package com.matrix.web.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.web.model.ApiAccessLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZhaoWeiLong
 * @since 2024/2/23
 **/
@FeignClient(contextId = "ApiAccessLogApi", value = ServerNameConstants.RESOURCE, path = "/apiAccessLog")
public interface ApiAccessLogApi {

  @PostMapping("/createApiAccessLog")
  void createApiAccessLog(@RequestBody ApiAccessLog apiAccessLog);
}
