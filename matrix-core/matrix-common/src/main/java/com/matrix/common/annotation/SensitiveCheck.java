package com.matrix.common.annotation;

import com.matrix.common.enums.SensitiveEnum;

import java.lang.annotation.*;

/**
 * 检测 支持文本、图片
 *
 * @author ZhaoWeiLong
 * @since 2023/4/21
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveCheck {


    /**
     * 检测类型
     */
    SensitiveEnum type() default SensitiveEnum.TEXT;

    /**
     * 模块名称 不指定默认为class名称
     */
    String moduleName() default "";


}
