package com.matrix.tenant.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 租户 Feign 客户端接口
 */
@FeignClient(contextId = "ITenantApi", value = ServerNameConstants.SYSTEM, path = "/sys/merchant")
public interface ITenantApi {

    /**
     * 获取所有租户 ID 列表
     */
    @GetMapping("/id-list")
    R<List<Long>> getTenantIdList();

    /**
     * 校验租户是否有效
     *
     * @param id 租户编号
     */
    @GetMapping("/valid")
    R<Boolean> validTenant(@RequestParam("id") Long id);
}
