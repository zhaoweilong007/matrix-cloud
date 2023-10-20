package com.matrix.translation.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 标签服务
 */
@FeignClient(contextId = "ILabelNameService", value = ServerNameConstants.HOUSE)
public interface ILabelNameService {

    /**
     * 通过标签ID查询标签名称
     *
     * @param id 标签ID
     * @return 结果
     */
    @GetMapping("/rem/labelInfo/selectNameByLabelId")
    R<String> selectNameByLabelId(@RequestParam("id") Long id);

}
