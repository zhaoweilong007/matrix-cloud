package com.matrix.mail.config;

import com.matrix.mail.core.MailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件发送自动配置，初始化 MailBuilder Bean
 */
@AutoConfiguration
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = "matrix.mail", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MailAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MailAutoConfiguration.class);

    /**
     * 创建邮件链式构建器 Bean
     *
     * @param mailSender JavaMail 发送器
     * @param properties 邮件配置属性
     * @return MailBuilder 实例
     */
    @Bean
    public MailBuilder mailBuilder(JavaMailSender mailSender, MailProperties properties) {
        log.info("Mail module initialized: from={}, fromName={}",
                properties.getFrom() != null ? properties.getFrom() : "(using spring.mail.username)",
                properties.getFromName());
        return new MailBuilder(mailSender, properties);
    }
}
