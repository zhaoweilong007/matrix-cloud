package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 首页推荐展示内容类型
 *
 * @author LeonZhou
 * @since 2023/7/7
 **/
@Getter
@RequiredArgsConstructor
public enum ContentTypeEnum {

    DEFAULT("0", "默认"),
    TOP("1", "置顶");

    public final String code;
    public final String name;

    /**
     * 根据code获取枚举
     */
    public static String codeToName(String code) {
        if (null != code) {
            for (ContentTypeEnum e : ContentTypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getName();
                }
            }
        }
        return null;
    }
}
