package com.matrix.excel.core;

import org.apache.fesod.sheet.read.listener.ReadListener;

/**
 * Excel 导入监听
 */
public interface ExcelListener<T> extends ReadListener<T> {

    /**
     * 获取导入结果
     */
    ExcelResult<T> getExcelResult();
}
