package com.matrix.exception;


import com.matrix.entity.vo.Result;

public class ServiceException extends BaseException {

    //TODO 对业务异常的返回码进行校验，规范到一定范围内

    public ServiceException(ErrorType errorType) {
        super(errorType);
    }

    public ServiceException(Result result) {
        super(new ErrorType() {
            @Override
            public Integer getCode() {
                return result.getCode();
            }

            @Override
            public String getMsg() {
                return result.getMsg();
            }
        });
    }

}
