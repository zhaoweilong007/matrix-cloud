package com.matrix.common.exception;

import com.matrix.common.exception.base.BaseException;
import com.matrix.common.result.R;
import net.dreamlu.mica.core.result.IResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务服务异常类，继承 {@link BaseException}，用于在服务层抛出可识别的业务异常。
 * <p>
 * 支持通过 {@link IResultCode}、{@link com.matrix.common.result.R R<?>}、
 * 状态码与消息等多种方式构造异常，最终由全局异常处理器统一捕获并返回标准响应。
 * </p>
 */
public class ServiceException extends BaseException {

    /** 有效错误码下限 */
    private static final int CODE_MIN = 1000;
    /** 有效错误码上限 */
    private static final int CODE_MAX = 9999;

    public ServiceException(IResultCode resultCode) {
        super(resultCode);
        validateCode(resultCode.getCode());
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

    /**
     * 校验错误码范围，超出 [1000, 9999] 时日志警告。
     *
     * @param code 错误码
     */
    private static void validateCode(int code) {
        if (code < CODE_MIN || code > CODE_MAX) {
            Logger log = LoggerFactory.getLogger(ServiceException.class);
            log.warn("错误码 {} 超出规范范围 [{}, {}]，请检查 IResultCode 定义", code, CODE_MIN, CODE_MAX);
        }
    }
}
