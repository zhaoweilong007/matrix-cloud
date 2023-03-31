package com.matrix.convert;

import com.matrix.api.resource.model.FileInfo;
import com.matrix.api.resource.model.MailMessageReq;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author ZhaoWeiLong
 * @since 2023/3/31
 **/
@Mapper
public interface ResourceConvert {

  ResourceConvert INSTANCE = Mappers.getMapper(ResourceConvert.class);


  FileInfo convert(cn.xuyanwu.spring.file.storage.FileInfo fileInfo);
  SimpleMailMessage convert(MailMessageReq mailMessageReq);


}
