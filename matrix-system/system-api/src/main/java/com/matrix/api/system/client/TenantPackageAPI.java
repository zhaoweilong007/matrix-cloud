package com.matrix.api.system.client;

import com.matrix.api.system.entity.po.SysTenantPackage;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 租户套餐 Feign API。
 */
@FeignClient(contextId = "TenantPackageAPI", value = "system-server", path = "/system/tenant-package")
public interface TenantPackageAPI {

    @Operation(summary = "套餐简化列表")
    @GetMapping("/list-all-simple")
    R<List<SysTenantPackage>> listAllSimple();

    @Operation(summary = "套餐详情")
    @GetMapping("/{id}")
    R<SysTenantPackage> getById(@PathVariable("id") Long id);
}
