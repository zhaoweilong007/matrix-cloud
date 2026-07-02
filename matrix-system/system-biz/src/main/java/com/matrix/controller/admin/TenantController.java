package com.matrix.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.TenantDto;
import com.matrix.api.system.entity.po.Tenant;
import com.matrix.common.result.R;
import com.matrix.convert.ConvertMapper;
import com.matrix.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理控制器。
 *
 * <p>提供租户的增删改查及用户分配功能。</p>
 */
@RestController
@RequestMapping("/tenant")
@Tag(name = "租户服务")
public class TenantController {

    /** 租户服务 */
    @Resource
    private TenantService tenantService;

    /**
     * 新增租户。
     *
     * @param tenantDto 租户信息
     * @return 是否成功
     */
    @PostMapping
    @Operation(summary = "新增租户")
    public R<Boolean> add(@RequestBody TenantDto tenantDto) {
        Tenant tenant = ConvertMapper.INSTALL.convert(tenantDto);
        return R.success(tenantService.save(tenant));
    }

    /**
     * 修改租户。
     *
     * @param tenantDto 租户信息
     * @return 是否成功
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改租户")
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody TenantDto tenantDto) {
        Tenant tenant = ConvertMapper.INSTALL.convert(tenantDto);
        tenant.setId(id);
        return R.success(tenantService.updateById(tenant));
    }

    /**
     * 删除租户。
     *
     * @param id 租户 ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除租户")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(tenantService.removeById(id));
    }

    /**
     * 查询租户详情。
     *
     * @param id 租户 ID
     * @return 租户信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询租户详情")
    public R<Tenant> detail(@PathVariable("id") Long id) {
        return R.success(tenantService.getById(id));
    }

    /**
     * 分页查询租户列表。
     *
     * @param page 分页参数
     * @return 租户分页数据
     */
    @GetMapping("list")
    @Operation(summary = "租户列表")
    public R<Page<Tenant>> list(Page<Tenant> page) {
        return R.success(tenantService.page(page));
    }

    /**
     * 分配用户到租户。
     *
     * @param userIds  用户 ID 列表
     * @param tenantId 租户 ID
     * @return 是否成功
     */
    @PostMapping("/assignUser")
    @Operation(summary = "分配用户到租户")
    public R<Boolean> assignUser(@RequestBody List<Long> userIds, @RequestParam Long tenantId) {
        return R.success(tenantService.assignUser(userIds, tenantId));
    }
}
