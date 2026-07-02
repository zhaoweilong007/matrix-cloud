package com.matrix.api.system.client;

import com.matrix.api.system.entity.vo.DeptTreeVo;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 部门服务 Feign API。
 */
@FeignClient(contextId = "DeptAPI", value = "system-server", path = "/system/dept")
public interface DeptAPI {

    @Operation(summary = "部门树形列表")
    @GetMapping("/list")
    R<List<DeptTreeVo>> list();

    @Operation(summary = "部门简化列表（下拉框）")
    @GetMapping("/list-all-simple")
    R<List<DeptTreeVo>> listAllSimple();

    @Operation(summary = "部门详情")
    @GetMapping("/{id}")
    R<DeptTreeVo> getById(@PathVariable("id") Long id);
}
