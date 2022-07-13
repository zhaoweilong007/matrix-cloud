package com.matrix.controller;

import com.matrix.entity.dto.SysRoleDto;
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

    /**
     * 分配菜单
     */
    @PostMapping("assignMenu")
    public Mono<Result<Boolean>> assignMenu(Long roleId, List<Long> menuIds) {
        return Mono.just(Result.success(roleService.assignMenu(roleId, menuIds)));
    }

    /**
     * 分配资源
     */
    @PostMapping("assignResource")
    public Mono<Result<Boolean>> assignResource(Long roleId, List<Long> resourceIds) {
        return Mono.just(Result.success(roleService.assignResource(roleId, resourceIds)));
    }
}
