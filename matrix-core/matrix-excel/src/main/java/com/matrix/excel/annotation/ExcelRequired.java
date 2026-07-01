package com.matrix.excel.annotation;

import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 必填标记注解。
 *
 * <p>导出时表头字体自动标红，提示该列为必填项。</p>
 *
 * @author matrix
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelRequired {

    /**
     * 表头字体颜色，默认红色
     */
    IndexedColors fontColor() default IndexedColors.RED;
}
