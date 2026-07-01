package com.matrix.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态下拉选项注解。
 *
 * <p>配合 {@code ExcelOptionsProvider} SPI 接口，在导出 Excel 时生成动态下拉框。
 * 通过指定 provider 类名从数据库/服务动态获取下拉选项。</p>
 *
 * <pre>
 * {@literal @}ExcelDynamicOptions(provider = DeptOptionsProvider.class)
 * private Long deptId;
 * </pre>
 *
 * @author matrix
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelDynamicOptions {

    /** 下拉选项提供者类（需实现 ExcelOptionsProvider 接口） */
    Class<?> provider();
}
