package com.matrix.mybatis.handler;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Mybatis异常处理器
 */
@Slf4j
@RestControllerAdvice
public class MybatisExceptionHandler {

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestURI, e.getMessage());
        return R.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public R<Void> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}', Mybatis系统异常", requestUri, e);
        return R.fail(SystemErrorTypeEnum.SYSTEM_ERROR);
    }


    @ExceptionHandler(BadSqlGrammarException.class)
    public R<Void> dadSqlGrammarExceptionHandler(BadSqlGrammarException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}', sql执行异常", requestUri, e);
        return R.fail(SystemErrorTypeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(PersistenceException.class)
    public R<Void> handlerIbatisException(PersistenceException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}', sql执行异常", requestUri, e);
        return R.fail(SystemErrorTypeEnum.SYSTEM_ERROR);
    }

}
