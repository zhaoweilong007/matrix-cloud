package com.matrix.api.system.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.po.SysDictType;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 字典类型 Feign API。
 */
@FeignClient(contextId = "DictTypeAPI", value = "system-server", path = "/system/dict-type")
public interface DictTypeAPI {

    @Operation(summary = "字典类型分页列表")
    @GetMapping("/page")
    R<Page<SysDictType>> page(Page<SysDictType> page);

    @Operation(summary = "字典类型简化列表")
    @GetMapping("/list-all-simple")
    R<List<SysDictType>> listAllSimple();

    @Operation(summary = "字典类型详情")
    @GetMapping("/{id}")
    R<SysDictType> getById(@PathVariable("id") Long id);
}
