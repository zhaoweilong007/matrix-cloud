package com.matrix.tenant.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "ITenantApi", value = ServerNameConstants.SYSTEM, path = "/sys/merchant")
public interface ITenantApi {

    @GetMapping("/id-list")
    R<List<Long>> getTenantIdList();

    @GetMapping("/valid")
    R<Boolean> validTenant(@RequestParam("id") Long id);

}