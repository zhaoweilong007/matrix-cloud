package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysOperateLog;
import com.matrix.common.result.R;
import com.matrix.service.SysOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/operate-log")
@Tag(name = "操作日志管理")
@RequiredArgsConstructor
public class OperateLogController {

    private final SysOperateLogService operateLogService;

    @GetMapping("/page")
    @Operation(summary = "操作日志分页列表")
    public R<Page<SysOperateLog>> page(Page<SysOperateLog> page) {
        return R.success(operateLogService.page(page,
                new LambdaQueryWrapper<SysOperateLog>().orderByDesc(SysOperateLog::getOperateTime)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "操作日志详情")
    public R<SysOperateLog> getById(@PathVariable Long id) { return R.success(operateLogService.getById(id)); }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(operateLogService.removeById(id)); }
}
