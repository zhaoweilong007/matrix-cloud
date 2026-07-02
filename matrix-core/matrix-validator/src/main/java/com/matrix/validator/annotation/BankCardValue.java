package com.matrix.validator.annotation;

import com.matrix.validator.BankCardValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验银行卡号格式（Luhn 算法）。
 */
@Documented
@Constraint(validatedBy = BankCardValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BankCardValue {

    String message() default "银行卡号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 是否必填 */
    boolean required() default true;
}
