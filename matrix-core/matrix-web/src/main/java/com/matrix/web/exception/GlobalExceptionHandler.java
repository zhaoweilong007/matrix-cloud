package com.matrix.web.exception;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.SystemCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理所有异常，主要是提供给 Filter 使用 因为 Filter 不走 SpringMVC 的流程，但是我们又需要兜底处理异常，所以这里提供一个全量的异常处理过程，保持逻辑统一。
     *
     * @param request 请求
     * @param ex      异常
     * @return 通用返回
     */
    public R<?> allExceptionHandler(HttpServletRequest request, Throwable ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            return missingServletRequestParameterExceptionHandler(request, (MissingServletRequestParameterException) ex);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return methodArgumentTypeMismatchExceptionHandler(request, (MethodArgumentTypeMismatchException) ex);
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return methodArgumentNotValidExceptionExceptionHandler(request, (MethodArgumentNotValidException) ex);
        }
        if (ex instanceof BindException) {
            return bindExceptionHandler((BindException) ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return constraintViolationExceptionHandler((ConstraintViolationException) ex, request);
        }
        if (ex instanceof NoHandlerFoundException) {
            return noHandlerFoundExceptionHandler((NoHandlerFoundException) ex);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return httpRequestMethodNotSupportedExceptionHandler((HttpRequestMethodNotSupportedException) ex, request);
        }
        if (ex instanceof ServiceException) {
            return serviceExceptionHandler(request, (ServiceException) ex);
        }

        return defaultExceptionHandler(request, ex);
    }

    @ExceptionHandler(FeignException.class)
    public R<?> handlerFeignException(HttpServletRequest request, FeignException ex) {
        log.warn("[FeignException][uri({}/{}) feign调用异常:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        return R.fail(SystemErrorTypeEnum.FEIGN_INVOKE_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("[handleHttpMessageNotReadableException][uri({}/{}) 请求内容不正确:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, "请求内容不正确");
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R<?> missingServletRequestParameterExceptionHandler(HttpServletRequest request, MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler][uri({}/{}) 请求参数缺失:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> methodArgumentTypeMismatchExceptionHandler(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        log.warn("[methodArgumentTypeMismatchExceptionHandler][uri({}/{}) 请求参数类型错误:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodArgumentNotValidExceptionExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler][uri({}/{}) 参数校验不正确:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null; // 断言，避免告警
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, String.format("请求参数不正确,字段%s:%s", fieldError.getField(), fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public R<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<?> constraintViolationExceptionHandler(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("[constraintViolationExceptionHandler][uri({}/{}) :{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return R.fail(SystemErrorTypeEnum.ARGUMENT_NOT_VALID, String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }


    /**
     * 处理 SpringMVC 请求地址不存在
     * <p>
     * 注意，它需要设置如下两个配置项： 1. spring.mvc.throw-exception-if-no-handler-found 为 true 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<?> noHandlerFoundExceptionHandler(NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]:{}", ex.getMessage());
        return R.fail(SystemErrorTypeEnum.NOT_FOUND, String.format("请求地址不存在:%s", ex.getRequestURL()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * <p>
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler uri({}/{})]", request.getMethod(), request.getRequestURI(), ex);
        return R.fail(SystemCode.METHOD_NOT_SUPPORTED, String.format("请求方法不正确:%s", ex.getMessage()));
    }


    /**
     * 处理业务异常 ServiceException
     * <p>
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = ServiceException.class)
    public R<?> serviceExceptionHandler(HttpServletRequest request, ServiceException ex) {
        log.warn("[serviceExceptionHandler][uri({}/{}) msg:{} 业务异常:{}]", request.getRequestURI(), request.getMethod(), ex.getErrorType().getMsg(), ex.getStackTrace()[0]);
        return R.fail(ex.getErrorType(), ex.getMessage());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public R<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        log.error("[defaultExceptionHandler][uri({}/{}) 发生异常:]", req.getRequestURI(), req.getMethod(), ex);
        // 返回 ERROR R
        return R.fail(SystemErrorTypeEnum.SYSTEM_ERROR);
    }


}
