package com.matrix.mongodb.exception;

/**
 * mongo 异常类
 */
public class EasyMongoException extends RuntimeException {

    public EasyMongoException(String message) {
        super(message);
    }

    public EasyMongoException(Throwable throwable) {
        super(throwable);
    }

    public EasyMongoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
