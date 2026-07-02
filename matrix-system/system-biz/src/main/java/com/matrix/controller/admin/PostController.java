package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.PostDto;
import com.matrix.api.system.entity.po.SysPost;
import com.matrix.common.result.R;
import com.matrix.service.SysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/post")
@Tag(name = "岗位管理")
@RequiredArgsConstructor
public class PostController {

    private final SysPostService postService;

    @GetMapping("/page")
    @Operation(summary = "岗位分页列表")
    public R<Page<SysPost>> page(Page<SysPost> page) {
        return R.success(postService.page(page, new LambdaQueryWrapper<SysPost>().orderByAsc(SysPost::getSort)));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "岗位简化列表")
    public R<List<SysPost>> listAllSimple() {
        return R.success(postService.list(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getStatus, 1).orderByAsc(SysPost::getSort)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "岗位详情")
    public R<SysPost> getById(@PathVariable Long id) { return R.success(postService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增岗位")
    public R<Boolean> create(@RequestBody PostDto dto) {
        SysPost entity = new SysPost(); entity.setCode(dto.getCode()); entity.setName(dto.getName());
        entity.setSort(dto.getSort()); entity.setStatus(dto.getStatus()); entity.setRemark(dto.getRemark());
        return R.success(postService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改岗位")
    public R<Boolean> update(@RequestBody PostDto dto) {
        SysPost entity = new SysPost(); entity.setId(dto.getId()); entity.setCode(dto.getCode());
        entity.setName(dto.getName()); entity.setSort(dto.getSort()); entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        return R.success(postService.updateById(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除岗位")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(postService.removeById(id)); }
}
