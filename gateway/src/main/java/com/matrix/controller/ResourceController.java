package com.matrix.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.entity.dto.SysResourceDto;
import com.matrix.entity.po.SysResource;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 描述：资源控制器
 *
 * @author zwl
 * @since 2022/7/13 15:24
 **/
@Slf4j
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final SysResourceService sysResourceService;

    @PostMapping
    public Mono<Result<Boolean>> create(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return Mono.just(Result.success(sysResourceService.create(sysResourceDto)));
    }

    @PutMapping
    public Mono<Result<Boolean>> update(@Validated @RequestBody SysResourceDto sysResourceDto) {
        return Mono.just(Result.success(sysResourceService.update(sysResourceDto)));
    }


    @DeleteMapping("{id}")
    public Mono<Result<Boolean>> delete(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceService.removeById(id)));
    }


    @GetMapping("{id}")
    public Mono<Result<SysResource>> getById(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceService.getById(id)));
    }

    @PostMapping("/list")
    public Mono<Result<Page<SysResource>>> list(Page<SysResource> page, SysResource sysResource) {
        return Mono.just(Result.success(sysResourceService.page(page, new QueryWrapper<>(sysResource))));
    }


    /**
     * 根据用户id查询资源列表
     *
     * @param id 用户id
     */
    @GetMapping("/admin/{id}")
    public Mono<Result<List<SysResource>>> getResourceByAdminId(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceService.getResourceByAdminId(id)));
    }

    /**
     * 根据角色id查询资源列表
     *
     * @param id 角色id
     */
    @GetMapping("/role/{id}")
    public Mono<Result<List<SysResource>>> getResourceByRoleId(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceService.getResourceByRoleId(id)));
    }


    /**
     * 分配资源
     */
    @GetMapping("assignResource")
    public Mono<Result<Boolean>> assignResource(@RequestParam Long roleId,@RequestParam List<Long> resourceIds) {
        return Mono.just(Result.success(sysResourceService.assignResource(roleId, resourceIds)));
    }


    @GetMapping("listAll")
    public Mono<Result<List<SysResource>>> listAll() {
        return Mono.just(Result.success(sysResourceService.list()));
    }


}
