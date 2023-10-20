package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 高德API 枚举类
 *
 * @author LeonZhou
 * @since 2023/7/7
 **/
@Getter
@RequiredArgsConstructor
public enum AmapTypeEnum {

    SURROUND_METRO("150500", "地铁站"),
    ;

    public final String code;
    public final String name;
}
