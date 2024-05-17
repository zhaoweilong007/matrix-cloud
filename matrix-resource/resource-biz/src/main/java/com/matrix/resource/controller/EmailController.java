package com.matrix.resource.controller;

import com.matrix.api.resource.client.EmailApi;
import com.matrix.api.resource.vo.EmailVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EmailApi.PREFIX)
@Tag(name = "邮件服务", description = "邮件服务")
public class EmailController implements EmailApi {

    @Override
    public String sendEmail(EmailVo emailVo) {

        return "";
    }
}
