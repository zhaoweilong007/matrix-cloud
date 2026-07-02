package com.matrix.resource.controller;

import com.matrix.api.resource.client.EmailApi;
import com.matrix.api.resource.vo.EmailVo;
import com.matrix.mail.core.MailBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件服务控制器。
 *
 * <p>委托 {@link MailBuilder} 完成邮件发送。</p>
 */
@RestController
@RequestMapping(value = EmailApi.PREFIX)
@Tag(name = "邮件服务", description = "邮件服务")
@RequiredArgsConstructor
public class EmailController implements EmailApi {

    private final MailBuilder mailBuilder;

    @Override
    public String sendEmail(EmailVo emailVo) {
        mailBuilder.to(emailVo.getTo())
                .subject(emailVo.getSubject())
                .html(emailVo.getContent())
                .send();
        return "ok";
    }
}
