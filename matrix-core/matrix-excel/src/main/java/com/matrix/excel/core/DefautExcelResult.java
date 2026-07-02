package com.matrix.excel.core;

import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 * 默认excel返回对象
 *
 */
public class DefautExcelResult<T> implements ExcelResult<T> {

    /**
     * 数据对象list
     */
    @Setter
    private List<T> list;

    /**
     * 错误信息列表
     */
    @Setter
    private List<String> errorList;

    /**
     * 创建空的导入结果
     */
    public DefautExcelResult() {
        this.list = new ArrayList<>();
        this.errorList = new ArrayList<>();
    }

    /**
     * 创建指定数据和错误列表的导入结果
     *
     * @param list       导入数据列表
     * @param errorList  错误信息列表
     */
    public DefautExcelResult(List<T> list, List<String> errorList) {
        this.list = list;
        this.errorList = errorList;
    }

    /**
     * 根据已有导入结果创建副本
     *
     * @param excelResult 导入结果对象
     */
    public DefautExcelResult(ExcelResult<T> excelResult) {
        this.list = excelResult.getList();
        this.errorList = excelResult.getErrorList();
    }

    /**
     * 获取导入数据列表
     */
    @Override
    public List<T> getList() {
        return list;
    }

    /**
     * 获取错误信息列表
     */
    @Override
    public List<String> getErrorList() {
        return errorList;
    }

    /**
     * 获取导入回执
     *
     * @return 导入回执
     */
    @Override
    public String getAnalysis() {
        int successCount = list.size();
        int errorCount = errorList.size();
        if (successCount == 0) {
            return "读取失败，未解析到数据";
        } else {
            if (errorCount == 0) {
                return StrUtil.format("恭喜您，全部读取成功！共{}条", successCount);
            } else {
                return "";
            }
        }
    }
}
