package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 11:19
 **/
@FeignClient(value = "system-server", path = RoleAPI.PREFIX)
@Tag(name = "角色服务")
public interface RoleAPI {

    String PREFIX = "/role";

    @GetMapping("/list")
    @Operation(summary = "获取角色列表")
    R<Page<SysRole>> list(@SpringQueryMap Page<SysRole> page, @SpringQueryMap SysRole sysRole);

    @GetMapping("/assignRole")
    @Operation(summary = "分配角色")
    R<Boolean> assignRole(@RequestParam Long userId, @RequestParam List<Long> roleIds);


    @GetMapping("/admin/{id}")
    @Operation(summary = "根据用户id获取用户角色列表")
    R<List<SysRole>> getRoleByAdminId(@PathVariable("id") Long id);


    @PutMapping("/status/{id}/{status}")
    @Operation(summary = "更新角色状态")
    R<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
}
