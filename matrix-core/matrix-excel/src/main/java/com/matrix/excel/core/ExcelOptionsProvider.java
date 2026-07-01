package com.matrix.excel.core;

import java.util.List;

/**
 * 动态下拉选项数据提供者 SPI 接口。
 *
 * <p>配合 {@code @ExcelDynamicOptions} 注解使用，
 * 实现类注册为 Spring Bean 后自动被 Excel 导出处理器发现。</p>
 *
 * @author matrix
 */
public interface ExcelOptionsProvider {

    /**
     * 获取下拉选项列表。
     *
     * @return 选项字符串列表
     */
    List<String> getOptions();
}
