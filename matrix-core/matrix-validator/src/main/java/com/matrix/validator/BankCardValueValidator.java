package com.matrix.validator;

import cn.hutool.core.util.StrUtil;
import com.matrix.validator.annotation.BankCardValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 银行卡号校验器（Luhn 算法）。
 *
 * <p>校验银行卡号数字组成并通过 Luhn 校验码验证。</p>
 */
public class BankCardValueValidator implements ConstraintValidator<BankCardValue, String> {

    private boolean required;

    @Override
    public void initialize(BankCardValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return !required;
        }
        // 去空格，纯数字校验
        String card = value.replaceAll("\\s", "");
        if (!card.matches("\\d{13,19}")) {
            return false;
        }
        return luhnCheck(card);
    }

    /**
     * Luhn 算法校验。
     */
    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = cardNumber.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
}
