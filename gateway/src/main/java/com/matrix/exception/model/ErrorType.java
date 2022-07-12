package com.matrix.exception.model;

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
