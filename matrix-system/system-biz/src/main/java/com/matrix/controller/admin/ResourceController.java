package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.ResourceAPI;
import com.matrix.api.system.entity.dto.SysResourceDto;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysResourceService;
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
    public Result<Boolean> create(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return Result.success(sysResourceService.create(sysResourceDto));
    }

    @PutMapping
    public Result<Boolean> update(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return Result.success(sysResourceService.update(sysResourceDto));
    }


    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(sysResourceService.removeById(id));
    }


    @GetMapping("{id}")
    public Result<SysResource> getById(@PathVariable("id") Long id) {
        return Result.success(sysResourceService.getById(id));
    }

    @Override
    public Result<Page<SysResource>> list(Page<SysResource> page, SysResource sysResource) {
        return Result.success(sysResourceService.page(page, new QueryWrapper<>(sysResource)));
    }


    /**
     * 根据用户id查询资源列表
     *
     * @param id 用户id
     */
    @GetMapping("/admin/{id}")
    @Override
    public Result<List<SysResource>> getResourceByAdminId(@PathVariable("id") Long id) {
        log.debug("根据用户id查询资源列表,id={}", id);
        return Result.success(sysResourceService.getResourceByAdminId(id));
    }

    /**
     * 根据角色id查询资源列表
     *
     * @param id 角色id
     */
    @GetMapping("/role/{id}")
    @Override
    public Result<List<SysResource>> getResourceByRoleId(@PathVariable("id") Long id) {
        return Result.success(sysResourceService.getResourceByRoleId(id));
    }


    /**
     * 分配资源
     */
    @Override
    public Result<Boolean> assignResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        return Result.success(sysResourceService.assignResource(roleId, resourceIds));
    }


    @Override
    public Result<List<SysResource>> listAll() {
        return Result.success(sysResourceService.list());
    }


}
