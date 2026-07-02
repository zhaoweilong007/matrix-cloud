package com.matrix.api.system.client;

import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统配置 Feign API（供其他服务读取配置值）。
 */
@FeignClient(contextId = "ConfigAPI", value = "system-server", path = "/system/config")
public interface ConfigAPI {

    @Operation(summary = "按键名获取配置值")
    @GetMapping("/get-by-key/{key}")
    R<String> getByKey(@PathVariable("key") String key);
}
