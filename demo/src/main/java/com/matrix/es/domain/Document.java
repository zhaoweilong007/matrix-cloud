package com.matrix.es.domain;

import lombok.Data;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/9 17:53
 **/
@Data
public class Document {
    /**
     * es中的唯一id
     */
    private String id;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档内容
     */
    private String content;
}