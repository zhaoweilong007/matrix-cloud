package com.matrix.entity.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对多套 用户体系
 */
@Getter
@AllArgsConstructor
public enum UserType {

    /**
     * pc端
     */
    SYS_USER("sys_user"),

    /**
     * app端
     */
    APP_USER("app_user");

    private final String userType;

    public static UserType getUserType(String str) {
        for (UserType value : values()) {
            if (StrUtil.contains(str, value.getUserType())) {
                return value;
            }
        }
        return null;
    }
}
