package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysClient;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OAuth2 客户端 Feign API。
 */
@FeignClient(contextId = "ClientAPI", value = "system-server", path = "/system/client")
public interface ClientAPI {

    @Operation(summary = "客户端分页列表")
    @GetMapping("/page")
    R<Page<SysClient>> page(Page<SysClient> page);

    @Operation(summary = "客户端详情")
    @GetMapping("/{id}")
    R<SysClient> getById(@PathVariable("id") Long id);
}
