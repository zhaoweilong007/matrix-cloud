package com.matrix.exception.model;

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
    Authentication_failed(4000, "用户名或密码错误"),
    User_not_found(4001, "用户不存在"),
    User_has_been_locked(4002, "用户已被锁定");


    private final Integer code;
    private final String msg;
}
