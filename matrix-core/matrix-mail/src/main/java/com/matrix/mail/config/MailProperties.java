package com.matrix.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮件发送配置属性。
 *
 * <p>扩展 Spring Boot 标准 {@code spring.mail} 配置，增加发件人别名等。</p>
 *
 */
@Data
@ConfigurationProperties(prefix = "matrix.mail")
public class MailProperties {

    /** 是否启用邮件功能 */
    private boolean enabled = true;

    /** 默认发件人地址（覆盖 spring.mail.username） */
    private String from;

    /** 发件人显示名称 */
    private String fromName = "Matrix Cloud";
}
