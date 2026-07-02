package com.matrix.api.system.client;

import com.matrix.api.system.entity.po.SysSocial;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 社交用户 Feign API。
 */
@FeignClient(contextId = "SocialUserAPI", value = "system-server", path = "/system/social-user")
public interface SocialUserAPI {

    @Operation(summary = "用户绑定列表")
    @GetMapping("/bind-list")
    R<List<SysSocial>> bindList(@RequestParam Long userId);

    @Operation(summary = "社交绑定详情")
    @GetMapping("/{id}")
    R<SysSocial> getById(@PathVariable("id") Long id);
}
