package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.TenantPackageDto;
import com.matrix.api.system.entity.po.SysTenantPackage;
import com.matrix.common.result.R;
import com.matrix.service.SysTenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/tenant-package")
@Tag(name = "租户套餐管理")
@RequiredArgsConstructor
public class TenantPackageController {

    private final SysTenantPackageService tenantPackageService;

    @GetMapping("/page")
    @Operation(summary = "套餐分页列表")
    public R<Page<SysTenantPackage>> page(Page<SysTenantPackage> page) {
        return R.success(tenantPackageService.page(page));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "套餐简化列表")
    public R<List<SysTenantPackage>> listAllSimple() {
        return R.success(tenantPackageService.list(new LambdaQueryWrapper<SysTenantPackage>()
                .eq(SysTenantPackage::getStatus, 1)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "套餐详情")
    public R<SysTenantPackage> getById(@PathVariable Long id) { return R.success(tenantPackageService.getById(id)); }

    @PostMapping
    @Operation(summary = "新增套餐")
    public R<Boolean> create(@RequestBody TenantPackageDto dto) {
        SysTenantPackage entity = new SysTenantPackage();
        entity.setName(dto.getName()); entity.setStatus(dto.getStatus());
        entity.setMenuIds(dto.getMenuIds()); entity.setRemark(dto.getRemark());
        return R.success(tenantPackageService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改套餐")
    public R<Boolean> update(@RequestBody TenantPackageDto dto) {
        SysTenantPackage entity = new SysTenantPackage(); entity.setId(dto.getId());
        entity.setName(dto.getName()); entity.setStatus(dto.getStatus());
        entity.setMenuIds(dto.getMenuIds()); entity.setRemark(dto.getRemark());
        return R.success(tenantPackageService.updateById(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐")
    public R<Boolean> delete(@PathVariable Long id) { return R.success(tenantPackageService.removeById(id)); }
}
