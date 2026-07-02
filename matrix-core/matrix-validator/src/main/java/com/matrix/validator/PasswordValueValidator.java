package com.matrix.validator;

import cn.hutool.core.util.StrUtil;
import com.matrix.validator.annotation.PasswordValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码强度校验器。
 *
 * <p>校验密码长度、大小写字母、数字和特殊字符要求。</p>
 */
public class PasswordValueValidator implements ConstraintValidator<PasswordValue, String> {

    private int minLength;
    private int maxLength;
    private boolean requireUpper;
    private boolean requireLower;
    private boolean requireDigit;
    private boolean requireSpecial;

    @Override
    public void initialize(PasswordValue constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.requireUpper = constraintAnnotation.requireUpper();
        this.requireLower = constraintAnnotation.requireLower();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecial = constraintAnnotation.requireSpecial();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        if (value.length() < minLength || value.length() > maxLength) {
            return false;
        }
        if (requireUpper && !value.matches(".*[A-Z].*")) {
            return false;
        }
        if (requireLower && !value.matches(".*[a-z].*")) {
            return false;
        }
        if (requireDigit && !value.matches(".*\\d.*")) {
            return false;
        }
        if (requireSpecial && !value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }
}
