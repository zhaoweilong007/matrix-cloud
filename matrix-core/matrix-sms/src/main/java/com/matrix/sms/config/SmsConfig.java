package com.matrix.sms.config;

import com.matrix.sms.properties.SmsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author ZhaoWeiLong
 * @since 2024/1/18
 **/
@AutoConfiguration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {

}
