package com.matrix.api.system.client;

import com.matrix.api.system.entity.po.SysMenu;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 菜单 Feign API。
 */
@FeignClient(contextId = "MenuAPI", value = "system-server", path = "/menu")
public interface MenuAPI {

    @Operation(summary = "按角色ID查询菜单")
    @GetMapping("/getMenuByRoleId/{id}")
    R<List<SysMenu>> getMenuByRoleId(@PathVariable("id") Long id);

    @Operation(summary = "按用户ID查询菜单")
    @GetMapping("/getMenuByAdminId/{id}")
    R<List<SysMenu>> getMenuByAdminId(@PathVariable("id") Long id);

    @Operation(summary = "菜单树形列表")
    @GetMapping("/treeList")
    R<List<SysMenu>> treeList();

    @Operation(summary = "分配菜单给角色")
    @GetMapping("/assignMenu")
    R<Boolean> assignMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds);
}
