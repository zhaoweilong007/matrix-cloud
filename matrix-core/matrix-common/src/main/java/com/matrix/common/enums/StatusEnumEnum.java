package com.matrix.common.enums;

import lombok.Getter;

/**
 * 公共状态，一般用来表示开启和关闭
 *
 * @author aaronuu
 */
@Getter
public enum StatusEnumEnum {

    /**
     * 启用
     */
    ENABLE("enable", "启用"),

    /**
     * 禁用
     */
    DISABLE("disable", "禁用");

    private final String code;

    private final String message;

    StatusEnumEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取枚举
     */
    public static StatusEnumEnum codeToEnum(String code) {
        if (null != code) {
            for (StatusEnumEnum e : StatusEnumEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }

}
