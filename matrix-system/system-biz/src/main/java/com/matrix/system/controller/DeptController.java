package com.matrix.system.controller;

import com.matrix.api.system.entity.dto.DeptDto;
import com.matrix.api.system.entity.po.SysDept;
import com.matrix.api.system.entity.vo.DeptTreeVo;
import com.matrix.common.result.R;
import com.matrix.log.annotation.Log;
import com.matrix.log.enums.BusinessType;
import com.matrix.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 部门管理控制器。
 */
@RestController
@RequestMapping("/system/dept")
@Tag(name = "部门管理")
@RequiredArgsConstructor
public class DeptController {

    private final SysDeptService deptService;

    @GetMapping("/list")
    @Operation(summary = "部门树形列表")
    public R<List<DeptTreeVo>> list() {
        List<SysDept> tree = deptService.buildTree();
        return R.success(convertToTreeVo(tree));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "部门简化列表（下拉框）")
    public R<List<DeptTreeVo>> listAllSimple() {
        List<SysDept> tree = deptService.buildTree();
        return R.success(convertToTreeVo(tree));
    }

    @GetMapping("/{id}")
    @Operation(summary = "部门详情")
    public R<SysDept> getById(@PathVariable Long id) {
        return R.success(deptService.getById(id));
    }

    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增部门")
    public R<Boolean> create(@Validated @RequestBody DeptDto dto) {
        SysDept dept = new SysDept();
        dept.setParentId(dto.getParentId());
        dept.setName(dto.getName());
        dept.setLeader(dto.getLeader());
        dept.setPhone(dto.getPhone());
        dept.setEmail(dto.getEmail());
        dept.setSort(dto.getSort());
        dept.setStatus(dto.getStatus());
        return R.success(deptService.save(dept));
    }

    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改部门")
    public R<Boolean> update(@Validated @RequestBody DeptDto dto) {
        SysDept dept = new SysDept();
        dept.setId(dto.getId());
        dept.setParentId(dto.getParentId());
        dept.setName(dto.getName());
        dept.setLeader(dto.getLeader());
        dept.setPhone(dto.getPhone());
        dept.setEmail(dto.getEmail());
        dept.setSort(dto.getSort());
        dept.setStatus(dto.getStatus());
        return R.success(deptService.updateById(dept));
    }

    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    public R<Boolean> delete(@PathVariable Long id) {
        if (deptService.hasChildren(id)) {
            return R.fail("存在子部门，无法删除");
        }
        return R.success(deptService.removeById(id));
    }

    private List<DeptTreeVo> convertToTreeVo(List<SysDept> depts) {
        if (depts == null) return new ArrayList<>();
        return depts.stream().map(d -> {
            DeptTreeVo vo = new DeptTreeVo();
            vo.setId(d.getId());
            vo.setParentId(d.getParentId());
            vo.setName(d.getName());
            vo.setSort(d.getSort());
            vo.setStatus(d.getStatus());
            vo.setChildren(convertToTreeVo(d.getChildren()));
            return vo;
        }).collect(Collectors.toList());
    }
}
