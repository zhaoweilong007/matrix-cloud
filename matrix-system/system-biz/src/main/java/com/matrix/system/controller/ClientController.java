package com.matrix.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.SysClientDto;
import com.matrix.api.system.entity.po.SysClient;
import com.matrix.common.result.R;
import com.matrix.system.service.SysClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/client")
@Tag(name = "OAuth2客户端管理")
@RequiredArgsConstructor
public class ClientController {

    private final SysClientService clientService;

    @GetMapping("/page")
    @Operation(summary = "客户端分页列表")
    public R<Page<SysClient>> page(Page<SysClient> page) { return R.success(clientService.page(page)); }

    @GetMapping("/{id}")
    @Operation(summary = "客户端详情")
    public R<SysClient> getById(@PathVariable Long id) { return R.success(clientService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增客户端")
    public R<Boolean> create(@Validated @RequestBody SysClientDto dto) {
        SysClient e = new SysClient(); e.setClientId(dto.getClientId()); e.setClientKey(dto.getClientKey());
        e.setClientSecret(dto.getClientSecret()); e.setGrantTypes(dto.getGrantTypes());
        e.setDeviceType(dto.getDeviceType()); e.setActiveTimeout(dto.getActiveTimeout());
        e.setTimeout(dto.getTimeout()); e.setStatus(dto.getStatus()); e.setRemark(dto.getRemark());
        return R.success(clientService.save(e));
    }

    @PutMapping
    @Operation(summary = "修改客户端")
    public R<Boolean> update(@Validated @RequestBody SysClientDto dto) {
        SysClient e = new SysClient(); e.setId(dto.getId()); e.setClientId(dto.getClientId());
        e.setClientKey(dto.getClientKey()); e.setClientSecret(dto.getClientSecret());
        e.setGrantTypes(dto.getGrantTypes()); e.setDeviceType(dto.getDeviceType());
        e.setActiveTimeout(dto.getActiveTimeout()); e.setTimeout(dto.getTimeout());
        e.setStatus(dto.getStatus()); e.setRemark(dto.getRemark());
        return R.success(clientService.updateById(e));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户端")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(clientService.removeById(id)); }
}
