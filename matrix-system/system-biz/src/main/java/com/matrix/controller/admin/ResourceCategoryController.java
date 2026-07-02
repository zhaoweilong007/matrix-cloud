package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysResourceCategory;
import com.matrix.common.result.R;
import com.matrix.service.SysResourceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源分类管理控制器。
 *
 * <p>提供资源分类的增删改查功能。</p>
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/resource/category")
@Tag(name = "菜单分类服务")
public class ResourceCategoryController {

    private final SysResourceCategoryService sysResourceCategoryService;

    @PostMapping
    @Operation(summary = "新增资源分类")
    public R<Boolean> create(@Validated @RequestBody SysResourceCategory sysResourceCategory) {
        return R.success(sysResourceCategoryService.save(sysResourceCategory));
    }

    @PutMapping
    @Operation(summary = "修改资源分类")
    public R<Boolean> update(@Validated @RequestBody SysResourceCategory sysResourceCategory) {
        return R.success(sysResourceCategoryService.updateById(sysResourceCategory));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除资源分类")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(sysResourceCategoryService.removeById(id));
    }

    @GetMapping("{id}")
    @Operation(summary = "查询资源分类详情")
    public R<SysResourceCategory> getById(@PathVariable("id") Long id) {
        return R.success(sysResourceCategoryService.getById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询资源分类")
    public R<Page<SysResourceCategory>> getList(Page<SysResourceCategory> page, SysResourceCategory sysResourceCategory) {
        return R.success(sysResourceCategoryService.page(page, new QueryWrapper<>(sysResourceCategory)));
    }

    @GetMapping("/listAll")
    @Operation(summary = "查询所有资源分类")
    public R<List<SysResourceCategory>> getListAll() {
        return R.success(sysResourceCategoryService.list());
    }
}
