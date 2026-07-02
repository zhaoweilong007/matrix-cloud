package com.matrix.common.exception;

import com.matrix.common.exception.base.BaseException;
import com.matrix.common.result.R;
import net.dreamlu.mica.core.result.IResultCode;

/**
 * 业务服务异常类，继承 {@link BaseException}，用于在服务层抛出可识别的业务异常。
 * <p>
 * 支持通过 {@link IResultCode}、{@link com.matrix.common.result.R R<?>}、
 * 状态码与消息等多种方式构造异常，最终由全局异常处理器统一捕获并返回标准响应。
 */
public class ServiceException extends BaseException {

    // TODO 对业务异常的返回码进行校验，规范到一定范围内
    public ServiceException(IResultCode resultCode) {
        super(resultCode);
    }

    public ServiceException(R<?> r) {
        super(new IResultCode() {
            @Override
            public int getCode() {
                return r.getCode();
            }

            @Override
            public String getMsg() {
                return r.getMessage();
            }
        });
    }

    public ServiceException(IResultCode resultCode, Object[] args) {
        super(resultCode, args);
    }

    public ServiceException(IResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public ServiceException(IResultCode resultCode, String message, Object[] args) {
        super(resultCode, message, args);
    }

    public ServiceException(Integer code, String message) {
        super(new IResultCode() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return message;
            }
        });
    }
}
