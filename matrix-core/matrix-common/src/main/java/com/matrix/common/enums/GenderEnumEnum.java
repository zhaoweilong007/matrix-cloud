package com.matrix.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 性别的枚举
 *
 * @author aaronuu
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenderEnumEnum {

    /**
     * 男
     */
    M("M", "男"),

    /**
     * 女
     */
    F("F", "女");
    @EnumValue
    private final String code;

    @JsonValue
    private final String message;

    GenderEnumEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据gender获取性别数字标识 1 男 2 女 (老系统)
     */
    public static GenderEnumEnum numberToGender(Integer sex) {
        if (null != sex) {
            return sex == 1 ? GenderEnumEnum.M : GenderEnumEnum.F;
        }
        return GenderEnumEnum.M;
    }

    /**
     * 根据gender获取性别数字标识 1 男 2 女 (老系统)
     */
    public static Integer genderToNumber(GenderEnumEnum gender) {
        if (null != gender) {
            return gender.equals(M) ? 1 : 2;
        }
        return null;
    }

    /**
     * 根据code获取枚举
     */
    public static GenderEnumEnum codeToEnum(String code) {
        if (null != code) {
            for (GenderEnumEnum e : GenderEnumEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 编码转化成中文含义
     */
    public static String codeToMessage(String code) {
        if (null != code) {
            for (GenderEnumEnum e : GenderEnumEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getMessage();
                }
            }
        }
        return "未知";
    }

    @JsonCreator
    public static GenderEnumEnum ofValue(String value) {
        for (GenderEnumEnum statusEnum : values()) {
            if (value.equals(statusEnum.getCode())) {
                return statusEnum;
            }
        }
        return null;
    }
}
