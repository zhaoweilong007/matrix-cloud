package com.matrix.api.resource.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 发送邮件请求
 *
 * @author ZhaoWeiLong
 * @since 2023/3/31
 **/
@Data
public class MailMessageReq implements Serializable {


    private String from;

    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private Date sentDate;

    private String subject;

    private String text;

}
