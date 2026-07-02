package com.matrix.api.system.client;

import com.matrix.api.system.entity.po.SysDictData;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 字典数据 Feign API（供 matrix-translation 等模块远程调用）。
 */
@FeignClient(contextId = "DictDataAPI", value = "system-server", path = "/system/dict-data")
public interface DictDataAPI {

    @Operation(summary = "按字典类型查询数据列表")
    @GetMapping("/list-all-simple")
    R<List<SysDictData>> listByType(@RequestParam String dictType);
}
