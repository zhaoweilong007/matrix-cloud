package com.matrix.sms.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * SMS 短信模块配置属性。
 *
 * <p>前缀 {@code matrix.sms}，支持动态刷新。</p>
 */
@ConfigurationProperties(prefix = "matrix.sms")
@Data
@RefreshScope
public class SmsProperties {

    /**
     * 验证码忽略白名单
     */
    private List<String> smsValidateIgnore = new ArrayList<>();
}
