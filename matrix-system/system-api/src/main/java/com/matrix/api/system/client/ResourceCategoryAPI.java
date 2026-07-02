package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysResourceCategory;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 资源分类 Feign API。
 */
@FeignClient(contextId = "ResourceCategoryAPI", value = "system-server", path = "/resource/category")
public interface ResourceCategoryAPI {

    @Operation(summary = "分页查询资源分类")
    @GetMapping("/list")
    R<Page<SysResourceCategory>> getList(Page<SysResourceCategory> page, SysResourceCategory category);

    @Operation(summary = "查询所有资源分类")
    @GetMapping("/listAll")
    R<List<SysResourceCategory>> listAll();
}
