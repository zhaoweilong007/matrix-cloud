package com.matrix.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginType {

    /**
     * 密码登录
     */
    PASSWORD("password"),

    /**
     * 短信登录
     */
    SMS("password"),

    /**
     * 小程序登录
     */
    wx("wx");


    private final String type;

}
