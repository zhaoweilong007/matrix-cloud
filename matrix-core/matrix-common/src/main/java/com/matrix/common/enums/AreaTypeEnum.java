package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 区域类型(1 省 2 市 3 区/县 4 小区/镇 5 村)
 *
 * @author LeonZhou
 * @since 2023/7/13
 **/
@Getter
@RequiredArgsConstructor
public enum AreaTypeEnum {

    PROVINCE(1, "省"),
    CITY(2, "市"),
    AREA(3, "区/县"),
    TOWN(4, "小区/镇"),
    VILLIAGE(5, "村");

    public final Integer code;
    public final String name;

    /**
     * 根据code获取枚举
     */
    public static String codeToName(Integer code) {
        if (null != code) {
            for (AreaTypeEnum e : AreaTypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getName();
                }
            }
        }
        return null;
    }
}
