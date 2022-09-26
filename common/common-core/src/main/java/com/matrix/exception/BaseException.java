package com.matrix.exception;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
public class BaseException extends RuntimeException {
    /**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;

    private final Object[] args;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
        this.args = null;
    }

    public BaseException(ErrorType errorType, Object[] args) {
        super(errorType.getMsg());
        this.errorType = errorType;
        this.args = args;
    }

    public BaseException(ErrorType errorType, String message, Object[] args) {
        super(message);
        this.errorType = errorType;
        this.args = args;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.errorType = errorType;
        this.args = args;
    }

    @Override
    public String getMessage() {
        MessageSource source = SpringUtil.getBean(MessageSource.class);
        if (source == null) {
            return super.getMessage();
        }
        return source.getMessage(errorType.getMsg(), args, LocaleContextHolder.getLocale());
    }
}
