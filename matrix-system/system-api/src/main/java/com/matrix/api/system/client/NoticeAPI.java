package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysNotice;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 通知公告 Feign API。
 */
@FeignClient(contextId = "NoticeAPI", value = "system-server", path = "/system/notice")
public interface NoticeAPI {

    @Operation(summary = "通知分页列表")
    @GetMapping("/page")
    R<Page<SysNotice>> page(Page<SysNotice> page);

    @Operation(summary = "通知详情")
    @GetMapping("/{id}")
    R<SysNotice> getById(@PathVariable("id") Long id);
}
