package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysMessage;
import com.matrix.common.result.R;
import com.matrix.service.SysMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/message")
@Tag(name = "站内消息管理")
@RequiredArgsConstructor
public class MessageController {

    private final SysMessageService messageService;

    @GetMapping("/page")
    @Operation(summary = "消息分页列表")
    public R<Page<SysMessage>> page(Page<SysMessage> page) {
        return R.success(messageService.page(page,
                new LambdaQueryWrapper<SysMessage>().orderByDesc(SysMessage::getCreatedAt)));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "未读消息数")
    public R<Long> unreadCount(@RequestParam Long userId) {
        return R.success(messageService.count(new LambdaQueryWrapper<SysMessage>()
                .eq(SysMessage::getUserId, userId).eq(SysMessage::getStatus, 0)));
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "标记已读")
    public R<Boolean> read(@PathVariable Long id) {
        SysMessage msg = new SysMessage(); msg.setId(id); msg.setStatus(1); msg.setReadTime(LocalDateTime.now());
        return R.success(messageService.updateById(msg));
    }

    @PutMapping("/read-all")
    @Operation(summary = "全部已读")
    public R<Boolean> readAll(@RequestParam Long userId) {
        SysMessage update = new SysMessage();
        update.setStatus(1);
        update.setReadTime(LocalDateTime.now());
        return R.success(messageService.update(update,
                new LambdaQueryWrapper<SysMessage>().eq(SysMessage::getUserId, userId).eq(SysMessage::getStatus, 0)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除消息")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(messageService.removeById(id)); }
}
