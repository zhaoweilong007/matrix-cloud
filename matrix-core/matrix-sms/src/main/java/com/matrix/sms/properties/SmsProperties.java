package com.matrix.sms.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author ZhaoWeiLong
 * @since 2024/1/18
 **/
@ConfigurationProperties(prefix = "sms")
@Data
@RefreshScope
public class SmsProperties {


  /**
   * 验证码忽略白名单
   */
  private List<String> smsValidateIgnore = new ArrayList<>();
}
