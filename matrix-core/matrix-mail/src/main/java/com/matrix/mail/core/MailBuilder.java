package com.matrix.mail.core;

import com.matrix.mail.config.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件链式构建器。
 *
 * <p>支持纯文本、HTML、附件、抄送/密送等。示例：</p>
 * <pre>
 * mailBuilder.to("user@example.com")
 *            .subject("测试邮件")
 *            .html("&lt;h1&gt;Hello&lt;/h1&gt;")
 *            .send();
 * </pre>
 *
 * @author matrix
 */
public class MailBuilder {

    private static final Logger log = LoggerFactory.getLogger(MailBuilder.class);

    private final JavaMailSender mailSender;
    private final MailProperties properties;

    private String[] to;
    private String subject;
    private String text;
    private String html;
    private String[] cc;
    private String[] bcc;
    private final List<File> attachments = new ArrayList<>();
    private boolean isHtml;

    public MailBuilder(JavaMailSender mailSender, MailProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    public MailBuilder to(String... to) {
        this.to = to;
        return this;
    }

    public MailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailBuilder text(String text) {
        this.text = text;
        this.isHtml = false;
        return this;
    }

    public MailBuilder html(String html) {
        this.html = html;
        this.isHtml = true;
        return this;
    }

    public MailBuilder cc(String... cc) {
        this.cc = cc;
        return this;
    }

    public MailBuilder bcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public MailBuilder attach(File file) {
        this.attachments.add(file);
        return this;
    }

    public MailBuilder attach(String filePath) {
        this.attachments.add(new File(filePath));
        return this;
    }

    /**
     * 发送邮件。
     */
    public void send() {
        if (to == null || to.length == 0) {
            throw new IllegalArgumentException("收件人不能为空");
        }
        try {
            if (isHtml || !attachments.isEmpty() || cc != null || bcc != null) {
                sendMimeMessage();
            } else {
                sendSimpleMessage();
            }
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            log.error("邮件发送失败: to={}, subject={}", to, subject, e);
            throw new com.matrix.common.exception.ServiceException(
                    com.matrix.common.enums.SystemErrorTypeEnum.OPERATE_FAIL);
        }
    }

    private void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(buildFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());
        mailSender.send(message);
        log.info("邮件发送成功: to={}, subject={}", to, subject);
    }

    private void sendMimeMessage() throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(buildFrom(), properties.getFromName());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(isHtml ? html : text, isHtml);
        helper.setSentDate(new Date());

        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        for (File file : attachments) {
            FileSystemResource resource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), resource);
        }

        mailSender.send(mimeMessage);
        log.info("邮件发送成功: to={}, subject={}, attachments={}", to, subject, attachments.size());
    }

    private String buildFrom() {
        if (properties.getFrom() != null && !properties.getFrom().isBlank()) {
            return properties.getFrom();
        }
        // 回退到 spring.mail.username
        return null; // null 时 Spring 使用默认发件人
    }
}
