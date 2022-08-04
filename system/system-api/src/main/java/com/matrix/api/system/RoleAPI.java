package com.matrix.api.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.component.FeignInterceptor;
import com.matrix.entity.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 11:19
 **/
@FeignClient(value = "system-server", path = RoleAPI.PREFIX)
@ApiOperation("角色服务")
public interface RoleAPI {

    String PREFIX = "/role";

    @GetMapping("/list")
    @ApiOperation("获取角色列表")
    Result<Page<SysRole>> list(@SpringQueryMap Page<SysRole> page, @SpringQueryMap SysRole sysRole);

    @GetMapping("/assignRole")
    @ApiOperation("分配角色")
    Result<Boolean> assignRole(@RequestParam Long userId, @RequestParam List<Long> roleIds);


    @GetMapping("/admin/{id}")
    @ApiOperation("根据用户id获取用户角色列表")
    Result<List<SysRole>> getRoleByAdminId(@PathVariable("id") Long id);


    @PutMapping("/status/{id}/{status}")
    @ApiOperation("更新角色状态")
    Result<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
}
