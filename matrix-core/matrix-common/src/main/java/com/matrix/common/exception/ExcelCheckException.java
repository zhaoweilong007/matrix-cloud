package com.matrix.common.exception;

import lombok.Data;

import java.util.List;

/**
 * 描述：<p></p>
 *
 * @author ZhaoWeiLong
 * @since 2023/3/2
 **/
@Data
public class ExcelCheckException extends RuntimeException {

    private ExcelCheckData<?> excelCheckData;

    public ExcelCheckException(String msg, ExcelCheckData<?> excelCheckData) {
        super(msg);
        this.excelCheckData = excelCheckData;
    }

    @Data
    public static class ExcelCheckData<T> {

        private final List<T> repeatData;

        private final List<T> noExistData;

        private final List<T> newData;


        public ExcelCheckData(List<T> repeatData, List<T> noExistData, List<T> newData) {
            this.repeatData = repeatData;
            this.noExistData = noExistData;
            this.newData = newData;
        }
    }
}

