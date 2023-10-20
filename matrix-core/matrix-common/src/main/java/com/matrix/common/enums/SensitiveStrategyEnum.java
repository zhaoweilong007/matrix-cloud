package com.matrix.common.enums;

import cn.hutool.core.util.DesensitizedUtil;
import com.matrix.common.util.DesensitizationUtil;
import lombok.AllArgsConstructor;

import java.util.function.Function;

/**
 * 脱敏策略
 *
 * @author Yjoioooo
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
     * 手机号脱敏 中间四位
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 手机号脱敏 后八位
     * PC public customer 公客
     */
    PHONE_PC(DesensitizationUtil::mobilePhone),

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
    BANK_CARD(DesensitizedUtil::bankCard);

    //可自行添加其他脱敏策略

    private final Function<String, String> desensitizer;

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
