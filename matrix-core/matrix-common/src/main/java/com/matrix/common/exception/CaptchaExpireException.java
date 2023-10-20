package com.matrix.common.exception;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.base.BaseException;

/**
 * 验证码失效异常类
 */
public class CaptchaExpireException extends BaseException {
    public CaptchaExpireException(Object... args) {
        super(SystemErrorTypeEnum.VERIFICATION_CODE_EXPIRE, args);
    }
}
