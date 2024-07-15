package com.matrix.common.exception.base;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.util.spring.MessageUtils;
import com.matrix.common.util.string.StringUtils;
import lombok.Getter;
import net.dreamlu.mica.core.result.IResultCode;


@Getter
public class BaseException extends RuntimeException {

    /**
     * 异常对应的错误类型
     */
    private final IResultCode errorType;

    private final Object[] args;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        super(SystemErrorTypeEnum.SYSTEM_ERROR.getMsg());
        this.errorType = SystemErrorTypeEnum.SYSTEM_ERROR;
        this.args = null;
    }

    public BaseException(int code, String msg, Object... args) {
        super(msg);
        this.errorType = new IResultCode() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        };
        this.args = args;
    }

    public BaseException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.errorType = resultCode;
        this.args = null;
    }

    public BaseException(IResultCode errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.args = null;
    }

    public BaseException(IResultCode errorType, Object[] args) {
        super(errorType.getMsg());
        this.errorType = errorType;
        this.args = args;
    }

    public BaseException(IResultCode errorType, String message, Object[] args) {
        super(message);
        this.errorType = errorType;
        this.args = args;
    }

    public BaseException(IResultCode errorType, String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.errorType = errorType;
        this.args = args;
    }


    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (!StringUtils.isEmpty(message)) {
            message = MessageUtils.message(message, args);
        }
        if (message == null) {
            message = errorType.getMsg();
        }
        return message;
    }

}
