package com.matrix.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传图片或视频到阿里云，需要匹配业务类型作为路径来保存
 *
 * @author dhong
 */
@Getter
@AllArgsConstructor
public enum AliyunMediaPathEnum {

    //未区分
    INDIST(0, "indist");


    private final Integer code;
    private final String message;

    /**
     * 根据code获取枚举
     */
    public static String getValueByCode(Integer code) {
        if (null != code) {
            for (AliyunMediaPathEnum e : AliyunMediaPathEnum.values()) {
                if (e.code.equals(code)) {
                    return e.message;
                }
            }
        }

        return INDIST.message;
    }

}
