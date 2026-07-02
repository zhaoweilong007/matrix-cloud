package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 13:46
 **/
@FeignClient(value = "system-server", path = ResourceAPI.PREFIX)
@Tag(name = "资源服务")
public interface ResourceAPI {

    String PREFIX = "/resource";

    @GetMapping("/list")
    R<Page<SysResource>> list(@SpringQueryMap Page<SysResource> page, @SpringQueryMap SysResource sysResource);

    /**
     * 根据用户id查询资源列表
     *
     * @param id 用户id
     */
    @GetMapping("/admin/{id}")
    R<List<SysResource>> getResourceByAdminId(@PathVariable("id") Long id);

    /**
     * 根据角色id查询资源列表
     *
     * @param id 角色id
     */
    @GetMapping("/role/{id}")
    R<List<SysResource>> getResourceByRoleId(@PathVariable("id") Long id);

    /**
     * 分配资源
     */
    @GetMapping("/assignResource")
    R<Boolean> assignResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds);

    @GetMapping("/listAll")
    R<List<SysResource>> listAll();

}
