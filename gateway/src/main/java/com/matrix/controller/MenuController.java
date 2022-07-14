package com.matrix.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.matrix.entity.dto.SysMenuDto;
import com.matrix.entity.po.SysMenu;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 描述：菜单控制器
 *
 * @author zwl
 * @since 2022/7/13 15:22
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final SysMenuService sysMenuService;

    /**
     * 创建菜单
     *
     * @param sysMenuDto
     * @return
     */
    @PostMapping
    public Mono<Result<Boolean>> create(@Validated @RequestBody SysMenuDto sysMenuDto) {
        return Mono.just(Result.success(sysMenuService.create(sysMenuDto)));
    }

    /**
     * 更新菜单
     *
     * @param sysMenuDto
     * @return
     */
    @PutMapping
    public Mono<Result<Boolean>> update(@Validated @RequestBody SysMenuDto sysMenuDto) {
        return Mono.just(Result.success(sysMenuService.update(sysMenuDto)));
    }


    @GetMapping("{id}")
    public Mono<Result<SysMenu>> getById(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysMenuService.getItem(id)));
    }

    /**
     * 分页查询菜单
     *
     * @param parentId 父级菜单id
     * @param pageDTO  分页对象
     * @return
     */
    @GetMapping("list/{parentId}")
    public Mono<Result<PageDTO<SysMenu>>> getList(@PathVariable("parentId") Long parentId, PageDTO<SysMenu> pageDTO) {
        return Mono.just(Result.success(sysMenuService.list(parentId, pageDTO)));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("treeList")
    public Mono<Result<List<SysMenu>>> getTreeList() {
        return Mono.just(Result.success(sysMenuService.treeList()));
    }

    /**
     * 更新显示状态
     *
     * @param id     菜单id
     * @param status 状态 1、0
     */
    @PutMapping("status/{id}/{status}")
    public Mono<Result<Boolean>> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return Mono.just(Result.success(sysMenuService.updateHidden(id, status)));
    }


    /**
     * 根据用户id获取菜单列表
     *
     * @param id 用户id
     */
    @GetMapping("getMenuByAdminId/{id}")
    public Mono<Result<List<SysMenu>>> getMenuByAdminId(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysMenuService.getMenuByAdminId(id)));
    }

    /**
     * 根据角色id获取菜单列表
     *
     * @param id 角色id
     */
    @GetMapping("getMenuByRoleId/{id}")
    public Mono<Result<List<SysMenu>>> getMenuByRoleId(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysMenuService.getMenuByRoleId(id)));
    }


    /**
     * 为角色分配菜单
     */
    @PostMapping("assignMenu")
    public Mono<Result<Boolean>> assignMenu(Long roleId, List<Long> menuIds) {
        return Mono.just(Result.success(sysMenuService.assignMenu(roleId, menuIds)));
    }


}
