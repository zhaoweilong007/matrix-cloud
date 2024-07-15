package com.matrix.excel.annotation;


import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项)
 * <p>
 * 需搭配 {@link com.matrix.excel.core.CellMergeStrategy} 策略使用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

    /**
     * col index
     */
    int index() default -1;

}
