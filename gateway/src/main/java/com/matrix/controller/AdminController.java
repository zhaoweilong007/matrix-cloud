package com.matrix.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.vo.Result;
import com.matrix.service.SysAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 描述：用户控制器
 *
 * @author zwl
 * @since 2022/7/14 14:32
 **/
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private SysAdminService sysAdminService;

    /**
     * 分页查询
     */
    @GetMapping("list")
    public Mono<Result<Page<SysAdmin>>> list(Page<SysAdmin> page, SysAdmin sysAdmin) {
        return Mono.just(Result.success(sysAdminService.page(page, new QueryWrapper<>(sysAdmin))));
    }


    @DeleteMapping("{id}")
    public Mono<Result<Boolean>> delete(@PathVariable("id") Long id) {
        return Mono.just(Result.success(sysAdminService.removeById(id)));
    }


    @PutMapping
    public Mono<Result<Boolean>> update(@RequestBody SysAdmin sysAdmin) {
        return Mono.just(Result.success(sysAdminService.updateById(sysAdmin)));
    }

    @GetMapping("userInfo")
    public Mono<SaResult> getUserInfo() {
        Long loginId = (Long) StpUtil.getLoginId();
        return Mono.just(SaResult.data(sysAdminService.userInfo(loginId)));
    }

    /**
     * 更新显示状态
     */
    @PutMapping("status/{id}/{status}")
    public Mono<Result<Boolean>> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return Mono.just(Result.success(sysAdminService.updateHidden(id, status)));
    }


}
