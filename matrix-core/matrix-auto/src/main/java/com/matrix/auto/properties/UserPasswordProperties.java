package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 用户密码策略配置属性。
 *
 * <p>控制登录失败锁定策略：密码错误达到最大次数后，
 * 账户将被临时锁定一段时间。</p>
 */
@Data
@ConfigurationProperties(prefix = "matrix.user.password")
public class UserPasswordProperties {

    /**
     * 密码最大错误次数，超过后账户锁定
     */
    private Integer maxRetryCount = 5;

    /**
     * 密码错误锁定时间（分钟）
     */
    private Integer lockTime = 10;
}
