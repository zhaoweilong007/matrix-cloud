package com.matrix.validator;

import cn.hutool.core.util.StrUtil;
import com.matrix.validator.annotation.EmailValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 邮箱格式校验器。
 *
 * <p>基于 RFC 5322 简化正则，校验邮箱基本格式。</p>
 */
public class EmailValueValidator implements ConstraintValidator<EmailValue, String> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private boolean required;

    @Override
    public void initialize(EmailValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return !required;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
