package com.matrix.sms.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对应configId
 *
 **/
public enum SmsBlendEnum {
    /** 阿里云短信 */
    ALIYUN();

    /**
     * 根据名称查找对应的枚举，忽略则返回 null。
     *
     * @param name 枚举名称
     * @return 匹配的枚举，未匹配返回 null
     */
    public static SmsBlendEnum ofName(String name) {
        return Arrays.stream(values())
                .filter(smsBlendEnum -> Objects.equals(smsBlendEnum.name(), name))
                .findFirst()
                .orElse(null);
    }
}
