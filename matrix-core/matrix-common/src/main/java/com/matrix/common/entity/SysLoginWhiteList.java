package com.matrix.common.entity;

import lombok.Data;

/**
 * 后台访问白名单entity
 */

@Data
public class SysLoginWhiteList {

    /**
     * ip
     */
    private String ip;

    /**
     * 描述
     */
    private String remarks;

    private Integer status;
}