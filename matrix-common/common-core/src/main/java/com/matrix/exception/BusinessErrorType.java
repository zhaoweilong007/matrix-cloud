package com.matrix.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/12 18:26
 **/
@Getter
@RequiredArgsConstructor
public enum BusinessErrorType implements ErrorType {


    /**
     * 业务异常
     */
    AUTHENTICATION_FAILED(4000, "用户名或密码错误"),

    PASSWORD_MISTAKE(4001, "密码错误"),

    USER_EXISTS(4001, "用户已存在"),
    USER_NOT_EXISTS(4001, "用户不存在"),
    USER_HAS_BEEN_LOCKED(4002, "用户已被锁定"),

    UPLOAD_ERROR(5000,"上传失败");


    private final Integer code;
    private final String msg;
}
