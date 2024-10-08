package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysResourceCategory;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysResourceCategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/14 16:28
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/resource/category")
@Api(tags = "菜单分类服务")
public class ResourceCategoryController {

    private final SysResourceCategoryService sysResourceCategoryService;


    @PostMapping
    public Mono<Result<Boolean>> create(@Validated @RequestBody SysResourceCategory sysResourceCategory) {
        return Mono.just(Result.success(sysResourceCategoryService.save(sysResourceCategory)));
    }

    @PutMapping
    public Mono<Result<Boolean>> update(@Validated @RequestBody SysResourceCategory sysResourceCategory) {
        return Mono.just(Result.success(sysResourceCategoryService.updateById(sysResourceCategory)));
    }


    @DeleteMapping("{id}")
    public Mono<Result<Boolean>> delete(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceCategoryService.removeById(id)));
    }


    @GetMapping("{id}")
    public Mono<Result<SysResourceCategory>> getById(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysResourceCategoryService.getById(id)));
    }


    @GetMapping("/list")
    public Mono<Result<Page<SysResourceCategory>>> getList(Page<SysResourceCategory> page, SysResourceCategory sysResourceCategory) {
        return Mono.just(Result.success(sysResourceCategoryService.page(page, new QueryWrapper<>(sysResourceCategory))));
    }


    @GetMapping("/listAll")
    public Mono<Result<List<SysResourceCategory>>> getListAll() {
        return Mono.just(Result.success(sysResourceCategoryService.list()));
    }

}
