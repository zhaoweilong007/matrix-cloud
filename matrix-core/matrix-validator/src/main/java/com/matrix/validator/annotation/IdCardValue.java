package com.matrix.validator.annotation;

import com.matrix.validator.IdCardValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验身份证号码格式（18 位）。
 *
 * <p>支持末位校验码算法（GB 11643-1999）。</p>
 */
@Documented
@Constraint(validatedBy = IdCardValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdCardValue {

    String message() default "身份证号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 是否必填 */
    boolean required() default true;
}
