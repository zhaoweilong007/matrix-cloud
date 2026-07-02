package com.matrix.validator.annotation;

import com.matrix.validator.EmailValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验邮箱格式。
 *
 * <p>支持 RFC 5322 标准邮箱格式验证。</p>
 */
@Documented
@Constraint(validatedBy = EmailValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValue {

    String message() default "邮箱格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 是否必填 */
    boolean required() default true;
}
