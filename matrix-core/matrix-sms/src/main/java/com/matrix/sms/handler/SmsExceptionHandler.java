package com.matrix.sms.handler;

import com.matrix.common.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * SMS 异常处理器，统一拦截 {@link SmsBlendException} 并返回友好提示。
 *
 */
@Slf4j
@RestControllerAdvice
public class SmsExceptionHandler {

    /**
     * 拦截短信发送异常，返回友好的错误提示。
     */
    @ExceptionHandler(SmsBlendException.class)
    public R<Void> handleSmsBlendException(SmsBlendException e, HttpServletRequest request) {
        log.error("请求地址'{}'发生短信发送异常", request.getRequestURI(), e);
        return R.fail("短信发送失败，请稍后再试...");
    }
}
