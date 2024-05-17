package com.matrix.api.resource.client;

import com.matrix.api.resource.vo.EmailVo;
import com.matrix.common.constant.ServerNameConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 邮件
 *
 * @author LeonZhou
 * @since 2023/6/25
 */
@FeignClient(contextId = "EmailApi", value = ServerNameConstants.RESOURCE, path = EmailApi.PREFIX)
public interface EmailApi {


    String PREFIX = "/email";

    /**
     * 邮件发送
     *
     * @param emailVo 邮件内容
     * @return
     */
    @Operation(summary = "邮件发送", description = "邮件发送")
    @PostMapping("/sendEmail")
    String sendEmail(@Valid @RequestBody EmailVo emailVo);

}
