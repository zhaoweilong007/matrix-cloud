package com.matrix.validator;

import cn.hutool.core.util.StrUtil;
import com.matrix.validator.annotation.IdCardValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 身份证号码校验器。
 *
 * <p>校验 18 位身份证的格式和末位校验码（GB 11643-1999）。</p>
 */
public class IdCardValueValidator implements ConstraintValidator<IdCardValue, String> {

    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CHECK_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    private boolean required;

    @Override
    public void initialize(IdCardValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return !required;
        }
        if (value.length() != 18) {
            return false;
        }
        // 前 17 位必须为数字
        String numPart = value.substring(0, 17);
        if (!numPart.matches("\\d{17}")) {
            return false;
        }
        // 校验码计算
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (numPart.charAt(i) - '0') * WEIGHT[i];
        }
        char expected = CHECK_CODE[sum % 11];
        return expected == Character.toUpperCase(value.charAt(17));
    }
}
