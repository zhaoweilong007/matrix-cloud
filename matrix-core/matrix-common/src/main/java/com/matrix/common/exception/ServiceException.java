package com.matrix.common.exception;


import com.matrix.common.exception.base.BaseException;
import com.matrix.common.result.R;
import net.dreamlu.mica.core.result.IResultCode;

/**
 * @author zwl
 */
public class ServiceException extends BaseException {

    //TODO 对业务异常的返回码进行校验，规范到一定范围内
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
