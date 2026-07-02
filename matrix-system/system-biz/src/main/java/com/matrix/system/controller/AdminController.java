package com.matrix.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.client.AdminAPI;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.common.result.R;
import com.matrix.system.service.SysAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：用户控制器
 *
 * @author zwl
 * @since 2022/7/14 14:32
 **/
@RestController
@Slf4j
@RequestMapping(AdminAPI.PREFIX)
public class AdminController implements AdminAPI {
    @Autowired
    private SysAdminService sysAdminService;

    /**
     * 分页查询
     */
    @GetMapping("list")
    @Override
    public R<Page<SysAdmin>> list(Page<SysAdmin> page, SysAdmin sysAdmin) {
        return R.success(sysAdminService.page(page, new QueryWrapper<>(sysAdmin)));
    }


    @DeleteMapping("{id}")
    @Override
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(sysAdminService.removeById(id));
    }


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
