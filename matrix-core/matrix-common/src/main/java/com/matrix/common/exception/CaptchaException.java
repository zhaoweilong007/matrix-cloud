package com.matrix.common.exception;


import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.base.BaseException;

/**
 * 验证码错误异常类
 */
public class CaptchaException extends BaseException {

    public CaptchaException(String msg) {
        super(SystemErrorTypeEnum.VERIFICATION_CODE_ERROR.getCode(), msg);
    }

    public CaptchaException() {
        super(SystemErrorTypeEnum.VERIFICATION_CODE_ERROR);
    }
}
