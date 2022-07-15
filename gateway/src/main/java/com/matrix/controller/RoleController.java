package com.matrix.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.entity.dto.SysRoleDto;
import com.matrix.entity.po.SysRole;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 描述：角色控制器
 *
 * @author zwl
 * @since 2022/7/13 15:22
 **/
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final SysRoleService roleService;

    /**
     * 添加角色
     */
    @PostMapping
    public Mono<Result<Boolean>> add(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return Mono.just(Result.success(roleService.add(sysRoleDto)));
    }

    @PutMapping
    public Mono<Result<Boolean>> update(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return Mono.just(Result.success(roleService.update(sysRoleDto)));
    }

    @DeleteMapping("{id}")
    public Mono<Result<Boolean>> delete(@PathVariable("id") Long id) {
        return Mono.just(Result.success(roleService.removeById(id)));
    }


    @GetMapping("list")
    public Mono<Result<Page<SysRole>>> list(Page<SysRole> page, SysRole sysRole) {
        return Mono.just(Result.success(roleService.page(page, new QueryWrapper<>(sysRole))));
    }

    @GetMapping("assignRole")
    public Mono<Result<Boolean>> assignRole(@RequestParam Long userId,@RequestParam List<Long> roleIds) {
        return Mono.just(Result.success(roleService.assignRole(userId, roleIds)));
    }

    /**
     * 获取用户角色列表
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("admin/{id}")
    public Mono<Result<List<SysRole>>> getRoleById(@PathVariable("id") Long id) {
        return Mono.just(Result.success(roleService.getRoleByAdminId(id)));
    }


    /**
     * 更新显示状态
     */
    @PutMapping("status/{id}/{status}")
    public Mono<Result<Boolean>> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return Mono.just(Result.success(roleService.updateHidden(id, status)));
    }
}
