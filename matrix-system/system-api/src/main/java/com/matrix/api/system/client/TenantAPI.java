package com.matrix.api.system.client;

import com.matrix.api.system.entity.po.Tenant;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 租户 Feign API。
 */
@FeignClient(contextId = "TenantAPI", value = "system-server", path = "/tenant")
public interface TenantAPI {

    @Operation(summary = "按ID查询租户")
    @GetMapping("/{id}")
    R<Tenant> detail(@PathVariable("id") Long id);
}
