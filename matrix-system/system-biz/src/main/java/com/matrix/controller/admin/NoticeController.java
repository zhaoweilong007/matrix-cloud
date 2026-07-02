package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.NoticeDto;
import com.matrix.api.system.entity.po.SysNotice;
import com.matrix.common.result.R;
import com.matrix.service.SysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/notice")
@Tag(name = "通知公告管理")
@RequiredArgsConstructor
public class NoticeController {

    private final SysNoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "通知分页列表")
    public R<Page<SysNotice>> page(Page<SysNotice> page) {
        return R.success(noticeService.page(page, new LambdaQueryWrapper<SysNotice>().orderByDesc(SysNotice::getCreatedAt)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "通知详情")
    public R<SysNotice> getById(@PathVariable Long id) { return R.success(noticeService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增通知")
    public R<Boolean> create(@RequestBody NoticeDto dto) {
        SysNotice entity = new SysNotice(); entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent()); entity.setType(dto.getType());
        entity.setStatus(dto.getStatus()); entity.setRemark(dto.getRemark());
        return R.success(noticeService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改通知")
    public R<Boolean> update(@RequestBody NoticeDto dto) {
        SysNotice entity = new SysNotice(); entity.setId(dto.getId());
        entity.setTitle(dto.getTitle()); entity.setContent(dto.getContent());
        entity.setType(dto.getType()); entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        return R.success(noticeService.updateById(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(noticeService.removeById(id)); }
}
