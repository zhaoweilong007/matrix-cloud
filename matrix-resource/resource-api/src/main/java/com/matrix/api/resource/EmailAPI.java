package com.matrix.api.resource;

import com.matrix.api.resource.model.MailMessageReq;
import com.matrix.entity.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:50
 **/
@FeignClient(value = "resource", path = EmailAPI.PREFIX)
@Api(tags = "邮箱服务")
public interface EmailAPI {
    String PREFIX = "/email";


    @PostMapping("/sendEmail")
    @ApiOperation("发送邮件")
    Result sendEmail(@RequestBody MailMessageReq mailMessageReq);


}
