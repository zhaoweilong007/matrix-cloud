package com.matrix.api.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.component.FeignInterceptor;
import com.matrix.entity.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 13:46
 **/
@FeignClient(value = "system-server", path = ResourceAPI.PREFIX,configuration = FeignInterceptor.class)
@Api(tags = "资源服务")
public interface ResourceAPI {

    String PREFIX = "/resource";

    @GetMapping("/list")
    Result<Page<SysResource>> list(@SpringQueryMap Page<SysResource> page, @SpringQueryMap SysResource sysResource);

    /**
     * 根据用户id查询资源列表
     *
     * @param id 用户id
     */
    @GetMapping("/admin/{id}")
    Result<List<SysResource>> getResourceByAdminId(@MatrixVariable("id") Long id);

    /**
     * 根据角色id查询资源列表
     *
     * @param id 角色id
     */
    @GetMapping("/role/{id}")
    Result<List<SysResource>> getResourceByRoleId(@MatrixVariable("id") Long id);

    /**
     * 分配资源
     */
    @GetMapping("/assignResource")
    Result<Boolean> assignResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds);

    @GetMapping("/listAll")
    Result<List<SysResource>> listAll();

}
