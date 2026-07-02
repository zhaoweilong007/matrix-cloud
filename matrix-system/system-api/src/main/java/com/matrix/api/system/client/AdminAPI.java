package com.matrix.api.system.client;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.SysAdminDto;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 11:14
 **/

@FeignClient(value = "system-server", path = AdminAPI.PREFIX)
@Tag(name = "用户服务")
public interface AdminAPI {

    String PREFIX = "/admin";

    @GetMapping("/list")
    @Operation(summary = "分页查询")
    R<Page<SysAdmin>> list(@SpringQueryMap Page<SysAdmin> page, @SpringQueryMap SysAdmin sysAdmin);

    @PostMapping
    @Operation(summary = "新增用户")
    R<Boolean> create(@RequestBody SysAdminDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "根据id删除")
    R<Boolean> delete(@PathVariable("id") Long id);

    @PutMapping
    @Operation(summary = "更新")
    R<Boolean> update(@RequestBody SysAdmin sysAdmin);

    @GetMapping("/userInfo")
    @Operation(summary = "获取用户信息")
    SaResult getUserInfo();

    /**
     * 更新显示状态
     */
    @PutMapping("/status/{id}/{status}")
    @Operation(summary = "更新用户状态")
    R<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
}
