package com.matrix.sms.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对应configId
 *
 * @author ZhaoWeiLong
 * @since 2023/10/19
 **/
public enum SmsBlendEnum {

    ALIYUN();


    public static SmsBlendEnum ofName(String name) {
        return Arrays.stream(values()).filter(smsBlendEnum -> Objects.equals(smsBlendEnum.name(), name)).findFirst()
            .orElse(null);
    }
}
