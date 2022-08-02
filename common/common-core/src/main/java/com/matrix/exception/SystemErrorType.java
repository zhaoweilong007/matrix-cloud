package com.matrix.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemErrorType implements ErrorType {

    /**
     * 系统异常
     */
    SYSTEM_ERROR(-1, "系统异常"),
    SYSTEM_BUSY(1000, "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE(1001, "服务未找到"),
    GATEWAY_ERROR(1002, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(1003, "网关超时"),

    ARGUMENT_NOT_VALID(2000, "请求参数校验不通过"),
    INVALID_TOKEN(2001, "无效token"),
    UPLOAD_FILE_SIZE_LIMIT(2002, "上传文件大小超过限制"),

    DUPLICATE_PRIMARY_KEY(3000, "唯一键冲突");

    /**
     * 错误类型码
     */
    private final Integer code;
    /**
     * 错误类型描述信息
     */
    private final String msg;


}
