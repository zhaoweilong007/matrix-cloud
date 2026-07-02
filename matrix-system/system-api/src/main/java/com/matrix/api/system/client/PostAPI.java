package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysPost;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 岗位服务 Feign API。
 */
@FeignClient(contextId = "PostAPI", value = "system-server", path = "/system/post")
public interface PostAPI {

    @Operation(summary = "岗位分页列表")
    @GetMapping("/page")
    R<Page<SysPost>> page(Page<SysPost> page);

    @Operation(summary = "岗位简化列表")
    @GetMapping("/list-all-simple")
    R<List<SysPost>> listAllSimple();

    @Operation(summary = "岗位详情")
    @GetMapping("/{id}")
    R<SysPost> getById(@PathVariable("id") Long id);
}
