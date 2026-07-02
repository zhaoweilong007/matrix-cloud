package com.matrix.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.client.RoleAPI;
import com.matrix.api.system.entity.dto.SysRoleDto;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.common.result.R;
import com.matrix.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：角色控制器
 *
 * @author zwl
 * @since 2022/7/13 15:22
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RoleAPI.PREFIX)
public class RoleController implements RoleAPI {

    private final SysRoleService roleService;

    @PostMapping
    public R<Boolean> add(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return R.success(roleService.add(sysRoleDto));
    }

    @PutMapping
    public R<Boolean> update(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return R.success(roleService.update(sysRoleDto));
    }

    @DeleteMapping("{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(roleService.removeById(id));
    }


    @Override
    public R<Page<SysRole>> list(Page<SysRole> page, SysRole sysRole) {
        return R.success(roleService.page(page, new QueryWrapper<>(sysRole)));
    }

    @Override
    public R<Boolean> assignRole(@RequestParam Long userId, @RequestParam List<Long> roleIds) {
        return R.success(roleService.assignRole(userId, roleIds));
    }


    @Override
    @GetMapping("admin/{id}")
    public R<List<SysRole>> getRoleByAdminId(@PathVariable("id") Long id) {
        return R.success(roleService.getRoleByAdminId(id));
    }


    @Override
    @PutMapping("status/{id}/{status}")
    public R<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return R.success(roleService.updateHidden(id, status));
    }
}
