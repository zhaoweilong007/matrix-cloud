package com.matrix.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.client.ResourceAPI;
import com.matrix.api.system.entity.dto.SysResourceDto;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.common.result.R;
import com.matrix.system.service.SysResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：资源控制器
 *
 * @author zwl
 * @since 2022/7/13 15:24
 **/
@Slf4j
@RestController
@RequestMapping(ResourceAPI.PREFIX)
@RequiredArgsConstructor
public class ResourceController implements ResourceAPI {

    private final SysResourceService sysResourceService;

    @PostMapping
    public R<Boolean> create(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return R.success(sysResourceService.create(sysResourceDto));
    }

    @PutMapping
    public R<Boolean> update(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return R.success(sysResourceService.update(sysResourceDto));
    }


    @DeleteMapping("{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(sysResourceService.removeById(id));
    }


    @GetMapping("{id}")
    public R<SysResource> getById(@PathVariable("id") Long id) {
        return R.success(sysResourceService.getById(id));
    }

    @Override
    public R<Page<SysResource>> list(Page<SysResource> page, SysResource sysResource) {
        return R.success(sysResourceService.page(page, new QueryWrapper<>(sysResource)));
    }


    /**
     * 根据用户id查询资源列表
     *
     * @param id 用户id
     */
    @GetMapping("/admin/{id}")
    @Override
    public R<List<SysResource>> getResourceByAdminId(@PathVariable("id") Long id) {
        log.debug("根据用户id查询资源列表,id={}", id);
        return R.success(sysResourceService.getResourceByAdminId(id));
    }

    /**
     * 根据角色id查询资源列表
     *
     * @param id 角色id
     */
    @GetMapping("/role/{id}")
    @Override
    public R<List<SysResource>> getResourceByRoleId(@PathVariable("id") Long id) {
        return R.success(sysResourceService.getResourceByRoleId(id));
    }


    /**
     * 分配资源
     */
    @Override
    public R<Boolean> assignResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        return R.success(sysResourceService.assignResource(roleId, resourceIds));
    }


    @Override
    public R<List<SysResource>> listAll() {
        return R.success(sysResourceService.list());
    }


}
