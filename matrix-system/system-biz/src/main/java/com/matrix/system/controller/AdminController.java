package com.matrix.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.client.AdminAPI;
import com.matrix.api.system.entity.dto.SysAdminDto;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import com.matrix.log.annotation.Log;
import com.matrix.log.enums.BusinessType;
import com.matrix.system.service.SysAdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：用户控制器
 *
 * @author zwl
 * @since 2022/7/14 14:32
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminAPI.PREFIX)
public class AdminController implements AdminAPI {

    private final SysAdminService sysAdminService;

    /**
     * 分页查询
     */
    @GetMapping("list")
    @Override
    public R<Page<SysAdmin>> list(Page<SysAdmin> page, SysAdmin sysAdmin) {
        return R.success(sysAdminService.page(page, new QueryWrapper<>(sysAdmin)));
    }


    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("{id}")
    @Override
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(sysAdminService.removeById(id));
    }


    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增用户")
    public R<Boolean> create(@Validated @RequestBody SysAdminDto dto) {
        boolean exists = sysAdminService.getOne(Wrappers.<SysAdmin>lambdaQuery()
                .eq(SysAdmin::getUsername, dto.getUsername())) != null;
        Assert.isTrue(!exists, () -> new ServiceException(BusinessErrorTypeEnum.USER_EXIST));
        SysAdmin admin = new SysAdmin();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setNickName(dto.getNickName());
        admin.setNote(dto.getNote());
        admin.setDeptId(dto.getDeptId());
        admin.setMobile(dto.getMobile());
        admin.setSex(dto.getSex());
        admin.setAvatar(dto.getAvatar());
        admin.setRemark(dto.getRemark());
        admin.setStatus(dto.getStatus());
        admin.setUserType(dto.getUserType());
        return R.success(sysAdminService.save(admin));
    }

    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @Override
    public R<Boolean> update(@RequestBody SysAdmin sysAdmin) {
        return R.success(sysAdminService.updateById(sysAdmin));
    }

    @GetMapping("userInfo")
    @Override
    public SaResult getUserInfo() {
        Long loginId = (Long) StpUtil.getLoginId();
        return SaResult.data(sysAdminService.userInfo(loginId));
    }

    /**
     * 更新显示状态
     */
    @PutMapping("status/{id}/{status}")
    @Override
    public R<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return R.success(sysAdminService.updateHidden(id, status));
    }


}
