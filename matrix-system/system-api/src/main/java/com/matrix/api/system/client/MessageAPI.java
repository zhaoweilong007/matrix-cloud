package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysMessage;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 站内消息 Feign API。
 */
@FeignClient(contextId = "MessageAPI", value = "system-server", path = "/system/message")
public interface MessageAPI {

    @Operation(summary = "消息分页列表")
    @GetMapping("/page")
    R<Page<SysMessage>> page(Page<SysMessage> page);

    @Operation(summary = "未读消息数")
    @GetMapping("/unread-count")
    R<Long> unreadCount(@RequestParam Long userId);
}
