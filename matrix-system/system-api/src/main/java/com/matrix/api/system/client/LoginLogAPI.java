package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysLoginLog;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 登录日志 Feign API。
 */
@FeignClient(contextId = "LoginLogAPI", value = "system-server", path = "/system/login-log")
public interface LoginLogAPI {

    @Operation(summary = "登录日志分页列表")
    @GetMapping("/page")
    R<Page<SysLoginLog>> page(Page<SysLoginLog> page);

    @Operation(summary = "登录日志详情")
    @GetMapping("/{id}")
    R<SysLoginLog> getById(@PathVariable("id") Long id);
}
