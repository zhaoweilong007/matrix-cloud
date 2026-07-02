package com.matrix.ip.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 行政区域类型枚举。
 *
 */
@Getter
@AllArgsConstructor
public enum AreaTypeEnum {

    /** 国家 */
    COUNTRY(1, "国家"),
    /** 省份/直辖市 */
    PROVINCE(2, "省份"),
    /** 城市 */
    CITY(3, "城市"),
    /** 区/县 */
    DISTRICT(4, "地区");

    private final Integer type;
    private final String name;

    /**
     * 根据类型值查找枚举
     */
    public static AreaTypeEnum valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (AreaTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
