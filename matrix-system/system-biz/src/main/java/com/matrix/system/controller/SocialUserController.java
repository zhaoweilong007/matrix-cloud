package com.matrix.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.SocialBindDto;
import com.matrix.api.system.entity.po.SysSocial;
import com.matrix.common.result.R;
import com.matrix.system.service.SysSocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/social-user")
@Tag(name = "社交用户管理")
@RequiredArgsConstructor
public class SocialUserController {

    private final SysSocialService socialService;

    @GetMapping("/page")
    @Operation(summary = "社交用户分页列表")
    public R<Page<SysSocial>> page(Page<SysSocial> page) {
        return R.success(socialService.page(page,
                new LambdaQueryWrapper<SysSocial>().orderByDesc(SysSocial::getCreatedAt)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "社交绑定详情")
    public R<SysSocial> getById(@PathVariable Long id) {
        return R.success(socialService.getById(id));
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定社交账号")
    public R<Boolean> bind(@Validated @RequestBody SocialBindDto dto) {
        SysSocial entity = new SysSocial();
        entity.setUserId(dto.getUserId());
        entity.setSource(dto.getSource());
        return R.success(socialService.save(entity));
    }

    @GetMapping("/bind-list")
    @Operation(summary = "用户绑定列表")
    public R<java.util.List<SysSocial>> bindList(@RequestParam Long userId) {
        return R.success(socialService.list(
                new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getUserId, userId)));
    }

    @DeleteMapping("/unbind/{id}")
    @Operation(summary = "解除绑定")
    public R<Boolean> unbind(@PathVariable Long id) { return R.success(socialService.removeById(id)); }
}
