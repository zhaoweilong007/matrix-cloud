package com.matrix.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.DictTypeDto;
import com.matrix.api.system.entity.po.SysDictData;
import com.matrix.api.system.entity.po.SysDictType;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import com.matrix.system.service.SysDictDataService;
import com.matrix.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/dict-type")
@Tag(name = "字典类型管理")
@RequiredArgsConstructor
public class DictTypeController {

    private final SysDictTypeService dictTypeService;
    private final SysDictDataService dictDataService;

    @GetMapping("/page")
    @Operation(summary = "字典类型分页列表")
    public R<Page<SysDictType>> page(Page<SysDictType> page) {
        return R.success(dictTypeService.page(page, new LambdaQueryWrapper<SysDictType>().orderByAsc(SysDictType::getType)));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "字典类型简化列表")
    public R<List<SysDictType>> listAllSimple() { return R.success(dictTypeService.list()); }

    @GetMapping("/{id}")
    @Operation(summary = "字典类型详情")
    public R<SysDictType> getById(@PathVariable Long id) { return R.success(dictTypeService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增字典类型")
    public R<Boolean> create(@RequestBody DictTypeDto dto) {
        SysDictType entity = new SysDictType();
        entity.setName(dto.getName()); entity.setType(dto.getType());
        entity.setStatus(dto.getStatus()); entity.setRemark(dto.getRemark());
        return R.success(dictTypeService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改字典类型")
    public R<Boolean> update(@RequestBody DictTypeDto dto) {
        SysDictType entity = new SysDictType(); entity.setId(dto.getId());
        entity.setName(dto.getName()); entity.setType(dto.getType());
        entity.setStatus(dto.getStatus()); entity.setRemark(dto.getRemark());
        return R.success(dictTypeService.updateById(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典类型")
    public R<Boolean> delete(@PathVariable Long id) {
        SysDictType dictType = dictTypeService.getById(id);
        if (dictType == null) throw new ServiceException(BusinessErrorTypeEnum.DICT_EXIST);
        if (dictDataService.count(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, dictType.getType())) > 0) {
            throw new ServiceException(BusinessErrorTypeEnum.DICT_ASSIGN);
        }
        return R.success(dictTypeService.removeById(id));
    }
}
