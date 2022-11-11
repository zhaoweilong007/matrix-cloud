package com.matrix.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.AdminAPI;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysAdminService;
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
    public Result<Page<SysAdmin>> list(Page<SysAdmin> page, SysAdmin sysAdmin) {
        return Result.success(sysAdminService.page(page, new QueryWrapper<>(sysAdmin)));
    }


    @DeleteMapping("{id}")
    @Override
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(sysAdminService.removeById(id));
    }


    @PutMapping
    @Override
    public Result<Boolean> update(@RequestBody SysAdmin sysAdmin) {
        return Result.success(sysAdminService.updateById(sysAdmin));
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
    public Result<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return Result.success(sysAdminService.updateHidden(id, status));
    }


}
