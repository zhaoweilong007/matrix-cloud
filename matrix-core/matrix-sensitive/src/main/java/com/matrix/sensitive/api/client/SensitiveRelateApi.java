package com.matrix.sensitive.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.sensitive.entity.SensitiveRelate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/23
 **/
@FeignClient(contextId = "SensitiveRelateApi", value = ServerNameConstants.RESOURCE, path = "/sensitive/relate")
public interface SensitiveRelateApi {

    @PostMapping("/save")
    void save(@RequestBody SensitiveRelate sensitiveRelate);
}
