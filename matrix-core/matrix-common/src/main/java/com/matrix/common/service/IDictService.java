package com.matrix.common.service;


import com.matrix.common.util.string.StringUtils;

/**
 * 字典服务服务
 */
public interface IDictService {

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    default String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, StringUtils.SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    default String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, StringUtils.SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    String getDictLabel(String dictType, String dictValue, String separator);

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    String getDictValue(String dictType, String dictLabel, String separator);

}
