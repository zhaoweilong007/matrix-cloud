package com.matrix.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.matrix.api.system.entity.dto.SysMenuDto;
import com.matrix.api.system.entity.po.SysMenu;
import com.matrix.common.result.R;
import com.matrix.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "菜单服务")
public class MenuController {
    private final SysMenuService sysMenuService;

    /**
     * 创建菜单
     *
     * @param sysMenuDto
     * @return
     */
    @PostMapping
    public R<Boolean> create(@Validated @RequestBody SysMenuDto sysMenuDto) {
        return R.success(sysMenuService.create(sysMenuDto));
    }

    /**
     * 更新菜单
     *
     * @param sysMenuDto
     * @return
     */
    @PutMapping
    public R<Boolean> update(@Validated @RequestBody SysMenuDto sysMenuDto) {
        return R.success(sysMenuService.update(sysMenuDto));
    }


    @GetMapping("{id}")
    public R<SysMenu> getById(@PathVariable("id") Long id) {
        return R.success(sysMenuService.getItem(id));
    }

    /**
     * 分页查询菜单
     *
     * @param parentId 父级菜单id
     * @param pageDTO  分页对象
     * @return
     */
    @GetMapping("list/{parentId}")
    public R<PageDTO<SysMenu>> getList(@PathVariable("parentId") Long parentId, PageDTO<SysMenu> pageDTO) {
        return R.success(sysMenuService.list(parentId, pageDTO));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("treeList")
    public R<List<SysMenu>> getTreeList() {
        return R.success(sysMenuService.treeList());
    }

    /**
     * 更新显示状态
     *
     * @param id     菜单id
     * @param status 状态 1、0
     */
    @PutMapping("status/{id}/{status}")
    public R<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return R.success(sysMenuService.updateHidden(id, status));
    }


    /**
     * 根据用户id获取菜单列表
     *
     * @param id 用户id
     */
    @GetMapping("getMenuByAdminId/{id}")
    public R<List<SysMenu>> getMenuByAdminId(@PathVariable("id") Long id) {
        return R.success(sysMenuService.getMenuByAdminId(id));
    }

    /**
     * 根据角色id获取菜单列表
     *
     * @param id 角色id
     */
    @GetMapping("getMenuByRoleId/{id}")
    public R<List<SysMenu>> getMenuByRoleId(@PathVariable("id") Long id) {
        return R.success(sysMenuService.getMenuByRoleId(id));
    }


    /**
     * 为角色分配菜单
     */
    @GetMapping("assignMenu")
    public R<Boolean> assignMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        return R.success(sysMenuService.assignMenu(roleId, menuIds));
    }


}
