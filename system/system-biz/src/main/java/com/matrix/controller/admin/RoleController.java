package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.RoleAPI;
import com.matrix.api.system.entity.dto.SysRoleDto;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysRoleService;
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
    public Result<Boolean> add(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return Result.success(roleService.add(sysRoleDto));
    }

    @PutMapping
    public Result<Boolean> update(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return Result.success(roleService.update(sysRoleDto));
    }

    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(roleService.removeById(id));
    }


    @Override
    public Result<Page<SysRole>> list(Page<SysRole> page, SysRole sysRole) {
        return Result.success(roleService.page(page, new QueryWrapper<>(sysRole)));
    }

    @Override
    public Result<Boolean> assignRole(@RequestParam Long userId, @RequestParam List<Long> roleIds) {
        return Result.success(roleService.assignRole(userId, roleIds));
    }


    @Override
    @GetMapping("admin/{id}")
    public Result<List<SysRole>> getRoleByAdminId(@PathVariable("id") Long id) {
        return Result.success(roleService.getRoleByAdminId(id));
    }


    @Override
    @PutMapping("status/{id}/{status}")
    public Result<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return Result.success(roleService.updateHidden(id, status));
    }
}
