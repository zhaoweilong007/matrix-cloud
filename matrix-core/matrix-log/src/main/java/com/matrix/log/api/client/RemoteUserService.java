package com.matrix.log.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/1
 **/
@FeignClient(contextId = "RemoteUserService", name = ServerNameConstants.SYSTEM, path = "/sys/user")
public interface RemoteUserService {

    @GetMapping("/selectAuditUserByUserId")
    R<String> selectAuditUserByUserId(@RequestParam("userId") Long userId);
}
