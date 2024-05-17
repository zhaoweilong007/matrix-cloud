package com.matrix.api.resource.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.push.enums.AppEnum;
import com.matrix.common.result.R;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 极光推送
 *
 * @author LeonZhou
 * @since 2023/6/20
 */
@FeignClient(contextId = "JPushApi", value = ServerNameConstants.RESOURCE, path = JPushApi.PREFIX)
public interface JPushApi {

    String PREFIX = "/jpush";


    /**
     * 通过token获取手机号
     *
     * @param token 令牌
     * @return {@link String}
     */
    @GetMapping("/getPhoneNumberByToken")
    R<String> getPhoneNumberByToken(@NotBlank @RequestParam AppEnum appEnum, @NotBlank @RequestParam String token);

}
