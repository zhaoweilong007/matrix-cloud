package com.matrix.validator.annotation;

import com.matrix.validator.PasswordValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验密码强度。
 *
 * <p>默认要求：最少 8 位，含大写字母、小写字母、数字、特殊字符。</p>
 */
@Documented
@Constraint(validatedBy = PasswordValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValue {

    String message() default "密码强度不足";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 最小长度，默认 8 */
    int minLength() default 8;

    /** 最大长度，默认 32 */
    int maxLength() default 32;

    /** 是否必须包含大写字母，默认 true */
    boolean requireUpper() default true;

    /** 是否必须包含小写字母，默认 true */
    boolean requireLower() default true;

    /** 是否必须包含数字，默认 true */
    boolean requireDigit() default true;

    /** 是否必须包含特殊字符，默认 true */
    boolean requireSpecial() default true;
}
