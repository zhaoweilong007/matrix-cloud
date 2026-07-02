package com.matrix.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.DictDataDto;
import com.matrix.api.system.entity.po.SysDictData;
import com.matrix.common.result.R;
import com.matrix.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/dict-data")
@Tag(name = "字典数据管理")
@RequiredArgsConstructor
public class DictDataController {

    private final SysDictDataService dictDataService;

    @GetMapping("/page")
    @Operation(summary = "字典数据分页列表")
    public R<Page<SysDictData>> page(Page<SysDictData> page, @RequestParam(required = false) String dictType) {
        LambdaQueryWrapper<SysDictData> qw = new LambdaQueryWrapper<SysDictData>()
                .eq(dictType != null, SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getSort);
        return R.success(dictDataService.page(page, qw));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "字典数据简化列表（按类型）")
    public R<List<SysDictData>> listAllSimple(@RequestParam String dictType) {
        return R.success(dictDataService.list(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 1)
                .orderByAsc(SysDictData::getSort)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "字典数据详情")
    public R<SysDictData> getById(@PathVariable Long id) { return R.success(dictDataService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增字典数据")
    public R<Boolean> create(@RequestBody DictDataDto dto) {
        SysDictData entity = new SysDictData(); entity.setDictType(dto.getDictType());
        entity.setLabel(dto.getLabel()); entity.setValue(dto.getValue());
        entity.setSort(dto.getSort()); entity.setStatus(dto.getStatus());
        entity.setColorType(dto.getColorType()); entity.setCssClass(dto.getCssClass());
        entity.setRemark(dto.getRemark());
        return R.success(dictDataService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改字典数据")
    public R<Boolean> update(@RequestBody DictDataDto dto) {
        SysDictData entity = new SysDictData(); entity.setId(dto.getId());
        entity.setDictType(dto.getDictType()); entity.setLabel(dto.getLabel());
        entity.setValue(dto.getValue()); entity.setSort(dto.getSort());
        entity.setStatus(dto.getStatus()); entity.setColorType(dto.getColorType());
        entity.setCssClass(dto.getCssClass()); entity.setRemark(dto.getRemark());
        return R.success(dictDataService.updateById(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典数据")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(dictDataService.removeById(id)); }
}
