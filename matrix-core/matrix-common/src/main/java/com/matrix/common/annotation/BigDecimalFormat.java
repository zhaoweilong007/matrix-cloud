package com.matrix.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.matrix.common.jackson.BigDecimalSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;
import java.math.RoundingMode;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/18
 **/
@Inherited
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonSerialize(using = BigDecimalSerializer.class)
public @interface BigDecimalFormat {


    /**
     * 处理格式
     */
    Format format() default Format.normal;

    /**
     * 倍数 默认100
     */
    int multiple() default 100;


    /**
     * 舍入模式 除以时指定
     */
    RoundingMode mode() default RoundingMode.HALF_UP;


    @Getter
    @RequiredArgsConstructor
    enum Format {

        /**
         * 不做任何处理
         */
        normal,

        /**
         * 乘以
         */
        multi,

        /**
         * 除以
         */
        divide;

    }
}
