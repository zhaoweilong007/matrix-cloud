package com.matrix.api.system;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.component.FeignInterceptor;
import com.matrix.entity.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/2 11:14
 **/

@FeignClient(value = "system-server", path = AdminAPI.PREFIX, configuration = FeignInterceptor.class)
@Api(tags = "用户服务")
public interface AdminAPI {

    String PREFIX = "/admin";

    @GetMapping("/list")
    @ApiOperation("分页查询")
    Result<Page<SysAdmin>> list(@SpringQueryMap Page<SysAdmin> page, @SpringQueryMap SysAdmin sysAdmin);

    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除")
    Result<Boolean> delete(@MatrixVariable("id") Long id);

    @PutMapping
    @ApiOperation("更新")
    Result<Boolean> update(@RequestBody SysAdmin sysAdmin);

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    SaResult getUserInfo();

    /**
     * 更新显示状态
     */
    @PutMapping("/status/{id}/{status}")
    @ApiOperation("更新用户状态")
    Result<Boolean> updateStatus(@MatrixVariable("id") Long id, @MatrixVariable("status") Integer status);
}
