package com.matrix.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备类型
 * 针对多套 用户体系
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * pc端
     */
    SYS_USER(1, "sys_user"),

    /**
     * app端
     */
    APP_USER(2, "app_user"),

    /**
     * 微信小程序
     */
    WX_USER(3, "wx_user"),


    /**
     * 微信小程序
     */
    TIKTOK_USER(4, "tiktok_user");

    private final Integer value;
    private final String userType;

    public static UserTypeEnum getUserType(String str) {
        for (UserTypeEnum value : values()) {
            if (StringUtils.contains(str, value.getUserType())) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + str);
    }
}
