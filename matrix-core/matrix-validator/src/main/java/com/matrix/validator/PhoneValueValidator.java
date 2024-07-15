package com.matrix.validator;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.validator.annotation.PhoneValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 校验手机号是否合法
 *
 * @author aaronuu
 */
public class PhoneValueValidator implements ConstraintValidator<PhoneValue, String> {

    private Boolean required;

    @Override
    public void initialize(PhoneValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String phoneValue, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(phoneValue)) {
            if (required) {
                return false;
            } else {
                return true;
            }
        } else {
            return ReUtil.isMatch(Validator.MOBILE, phoneValue);
        }
    }

}
