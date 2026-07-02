package com.matrix.common.enums;

import cn.hutool.core.util.DesensitizedUtil;
import com.matrix.common.util.DesensitizationUtil;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * 脱敏策略
 *
 */
@AllArgsConstructor
public enum SensitiveStrategyEnum {

    /**
     * 身份证脱敏
     */
    ID_CARD(s -> DesensitizedUtil.idCardNum(s, 3, 4)),

    /**
     * 人名脱敏
     */
    NAME(DesensitizedUtil::chineseName),

    /**
     * 中文名脱敏（保留姓，用*替换名）
     */
    CHINESE_NAME(s -> {
        if (s == null || s.isEmpty()) {
            return s;
        }
        if (s.length() <= 1) {
            return "*";
        }
        return s.charAt(0) + "*".repeat(s.length() - 1);
    }),

    /**
     * 手机号脱敏 中间四位
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 手机号脱敏 后八位
     * PC public customer 公客
     */
    PHONE_PC(DesensitizationUtil::mobilePhone),

    /**
     * 固定电话脱敏
     */
    FIXED_PHONE(s -> {
        if (s == null || s.isEmpty()) {
            return s;
        }
        // 区号-号码格式：010-12345678 -> 010-****5678
        if (s.contains("-")) {
            String[] parts = s.split("-", 2);
            if (parts.length == 2 && parts[1].length() > 4) {
                return parts[0] + "-" + "*".repeat(parts[1].length() - 4)
                        + parts[1].substring(parts[1].length() - 4);
            }
        }
        // 纯数字格式
        if (s.length() > 4) {
            return "*".repeat(s.length() - 4) + s.substring(s.length() - 4);
        }
        return "*".repeat(s.length());
    }),

    /**
     * 地址脱敏
     */
    ADDRESS(s -> DesensitizedUtil.address(s, 8)),

    /**
     * 邮箱脱敏
     */
    EMAIL(DesensitizedUtil::email),

    /**
     * 银行卡
     */
    BANK_CARD(DesensitizedUtil::bankCard),

    /**
     * 密码脱敏
     */
    PASSWORD(s -> "********"),

    /**
     * IP地址脱敏（保留前两段）
     */
    IP(s -> {
        if (s == null || s.isEmpty()) {
            return s;
        }
        int lastDot = s.lastIndexOf('.');
        if (lastDot > 0) {
            int prevDot = s.lastIndexOf('.', lastDot - 1);
            if (prevDot > 0) {
                return s.substring(0, prevDot) + ".*.*";
            }
        }
        return "***.***.***.***";
    }),

    /**
     * 车牌号脱敏（保留省份简称和城市代码）
     */
    LICENSE_PLATE(s -> {
        if (s == null || s.isEmpty()) {
            return s;
        }
        if (s.length() >= 2) {
            return s.substring(0, 2) + "*".repeat(s.length() - 2);
        }
        return "*".repeat(s.length());
    }),

    /**
     * 身份证脱敏（保留前4后4）
     */
    ID_CARD_FULL(s -> {
        if (s == null || s.length() < 8) {
            return s;
        }
        return s.substring(0, 4) + "*".repeat(s.length() - 8) + s.substring(s.length() - 4);
    });

    private final Function<String, String> desensitizer;

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
