package com.matrix.exception;

public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    Integer getCode();

    /**
     * 返回mesg
     *
     * @return
     */
    String getMsg();
}
