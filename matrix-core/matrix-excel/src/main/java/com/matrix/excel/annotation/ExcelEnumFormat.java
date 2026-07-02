package com.matrix.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 枚举格式化注解。
 *
 * <p>导出时自动将枚举 code 转换为对应 text，导入时自动将 text 转换为 code。</p>
 *
 * <pre>
 * {@literal @}ExcelEnumFormat(enumClass = StatusEnum.class, codeField = "code", textField = "desc")
 * private Integer status;
 * </pre>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelEnumFormat {

    /** 枚举类 */
    Class<? extends Enum<?>> enumClass();

    /** 枚举中表示 code 的字段名 */
    String codeField() default "code";

    /** 枚举中表示 text 的字段名 */
    String textField() default "desc";
}
