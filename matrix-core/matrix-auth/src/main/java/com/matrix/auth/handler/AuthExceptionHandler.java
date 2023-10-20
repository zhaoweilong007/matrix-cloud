package com.matrix.auth.handler;

import cn.dev33.satoken.exception.*;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/9
 **/
@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {


    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return R.fail(SystemErrorTypeEnum.FORBIDDEN);
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败'{}'", requestURI, e.getMessage());
        return R.fail(SystemErrorTypeEnum.FORBIDDEN);
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',认证失败'{}',无法访问系统资源", requestURI, e.getMessage());
        return R.fail(SystemErrorTypeEnum.UNAUTHORIZED);
    }

    /**
     * 无效认证
     */
    @ExceptionHandler(SameTokenInvalidException.class)
    public R<Void> handleSameTokenInvalidException(SameTokenInvalidException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',内网认证失败'{}',无法访问系统资源", requestURI, e.getMessage());
        return R.fail(SystemErrorTypeEnum.UNAUTHORIZED);
    }

    @ExceptionHandler({SaTokenException.class})
    private R<?> saTokenExceptionHandler(HttpServletRequest request, SaTokenException ex) {
        log.warn("[saTokenExceptionHandler][uri({}/{}) 认证异常:{}]", request.getRequestURI(), request.getMethod(), ex.getMessage());
        return R.fail(SystemErrorTypeEnum.UNAUTHORIZED);
    }
}
