package com.matrix.common.exception;

import com.matrix.common.result.R;
import net.dreamlu.mica.core.result.IResultCode;

/**
 * 错误码值对象
 * <p>
 * 封装错误码和错误消息，支持参数占位符格式化
 * </p>
 *
 * @author matrix
 */
public class ErrorCode implements IResultCode {

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息模板（支持 {} 占位符）
     */
    private final String messageTemplate;

    /**
     * 用户提示消息
     */
    private final String userTip;

    public ErrorCode(int code, String messageTemplate) {
        this(code, messageTemplate, null);
    }

    public ErrorCode(int code, String messageTemplate, String userTip) {
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.userTip = userTip;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return messageTemplate;
    }

    public String getUserTip() {
        return userTip;
    }

    /**
     * 创建 ServiceException
     */
    public ServiceException exception() {
        return new ServiceException(this);
    }

    /**
     * 创建 ServiceException（带格式化参数）
     */
    public ServiceException exception(Object... params) {
        return new ServiceException(this, formatMessage(params));
    }

    /**
     * 创建 R 失败响应
     */
    public R<?> toR() {
        return R.fail(this);
    }

    /**
     * 创建 R 失败响应（带格式化参数）
     */
    public R<?> toR(Object... params) {
        return R.fail(this, formatMessage(params));
    }

    /**
     * 格式化消息（替换 {} 占位符）
     */
    public String formatMessage(Object... params) {
        if (params == null || params.length == 0) {
            return messageTemplate;
        }
        String result = messageTemplate;
        for (Object param : params) {
            result = result.replaceFirst("\\{}", String.valueOf(param));
        }
        return result;
    }
}
