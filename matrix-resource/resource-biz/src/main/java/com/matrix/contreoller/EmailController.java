package com.matrix.contreoller;

import com.matrix.api.resource.EmailAPI;
import com.matrix.api.resource.model.MailMessageReq;
import com.matrix.convert.ResourceConvert;
import com.matrix.entity.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:56
 **/
@RestController
@RequestMapping(EmailAPI.PREFIX)
public class EmailController implements EmailAPI {

  @Autowired
  private JavaMailSender mailSender;


  @Override
  public Result sendEmail(@RequestBody MailMessageReq mailMessageReq) {
    mailSender.send(ResourceConvert.INSTANCE.convert(mailMessageReq));
    return Result.success();
  }

}
