package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysOperateLog;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 操作日志 Feign API。
 */
@FeignClient(contextId = "OperateLogAPI", value = "system-server", path = "/system/operate-log")
public interface OperateLogAPI {

    @Operation(summary = "操作日志分页列表")
    @GetMapping("/page")
    R<Page<SysOperateLog>> page(Page<SysOperateLog> page);

    @Operation(summary = "操作日志详情")
    @GetMapping("/{id}")
    R<SysOperateLog> getById(@PathVariable("id") Long id);
}
