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
 */
public class MailBuilder {

    private static final Logger log = LoggerFactory.getLogger(MailBuilder.class);

    /**
     * Spring 邮件发送器
     */
    private final JavaMailSender mailSender;
    /**
     * 邮件配置属性
     */
    private final MailProperties properties;

    /**
     * 收件人地址数组
     */
    private String[] to;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 纯文本内容
     */
    private String text;
    /**
     * HTML 内容
     */
    private String html;
    /**
     * 抄送地址数组
     */
    private String[] cc;
    /**
     * 密送地址数组
     */
    private String[] bcc;
    /**
     * 附件列表
     */
    private final List<File> attachments = new ArrayList<>();
    /**
     * 是否为 HTML 格式
     */
    private boolean isHtml;

    public MailBuilder(JavaMailSender mailSender, MailProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    /**
     * 设置收件人
     *
     * @param to 收件人地址
     * @return MailBuilder 实例
     */
    public MailBuilder to(String... to) {
        this.to = to;
        return this;
    }

    /**
     * 设置邮件主题
     *
     * @param subject 邮件主题
     * @return MailBuilder 实例
     */
    public MailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * 设置纯文本内容
     *
     * @param text 纯文本内容
     * @return MailBuilder 实例
     */
    public MailBuilder text(String text) {
        this.text = text;
        this.isHtml = false;
        return this;
    }

    /**
     * 设置 HTML 内容
     *
     * @param html HTML 内容
     * @return MailBuilder 实例
     */
    public MailBuilder html(String html) {
        this.html = html;
        this.isHtml = true;
        return this;
    }

    /**
     * 设置抄送地址
     *
     * @param cc 抄送地址
     * @return MailBuilder 实例
     */
    public MailBuilder cc(String... cc) {
        this.cc = cc;
        return this;
    }

    /**
     * 设置密送地址
     *
     * @param bcc 密送地址
     * @return MailBuilder 实例
     */
    public MailBuilder bcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    /**
     * 添加附件
     *
     * @param file 附件文件
     * @return MailBuilder 实例
     */
    public MailBuilder attach(File file) {
        this.attachments.add(file);
        return this;
    }

    /**
     * 根据文件路径添加附件
     *
     * @param filePath 附件文件路径
     * @return MailBuilder 实例
     */
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

    /**
     * 发送纯文本邮件（使用 SimpleMailMessage）
     */
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

    /**
     * 发送 MIME 格式邮件（支持 HTML、附件、抄送、密送）
     */
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

    /**
     * 构建发件人地址，优先使用配置的 from，否则使用 spring.mail.username
     *
     * @return 发件人地址
     */
    private String buildFrom() {
        if (properties.getFrom() != null && !properties.getFrom().isBlank()) {
            return properties.getFrom();
        }
        // 回退到 spring.mail.username
        return null; // null 时 Spring 使用默认发件人
    }
}
