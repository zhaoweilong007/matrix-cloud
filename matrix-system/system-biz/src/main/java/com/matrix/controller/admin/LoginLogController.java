package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysLoginLog;
import com.matrix.common.result.R;
import com.matrix.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/login-log")
@Tag(name = "登录日志管理")
@RequiredArgsConstructor
public class LoginLogController {

    private final SysLoginLogService loginLogService;

    @GetMapping("/page")
    @Operation(summary = "登录日志分页列表")
    public R<Page<SysLoginLog>> page(Page<SysLoginLog> page) {
        return R.success(loginLogService.page(page,
                new LambdaQueryWrapper<SysLoginLog>().orderByDesc(SysLoginLog::getLoginTime)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "登录日志详情")
    public R<SysLoginLog> getById(@PathVariable Long id) { return R.success(loginLogService.getById(id)); }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除登录日志")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(loginLogService.removeById(id)); }
}
