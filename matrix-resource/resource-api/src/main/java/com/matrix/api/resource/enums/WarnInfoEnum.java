package com.matrix.api.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Getter
@AllArgsConstructor
public enum WarnInfoEnum {

    INVALID_EXPIRATION_DATE(-9100L, "身份证有效日期不合法告警"),
    INCOMPLETE_BORDER(-9101L, "身份证边框不完整告警"),
    PHOTOCOPY_WARNING(-9102L, "身份证复印件告警"),
    REPHOTOGRAPH_WARNING(-9103L, "身份证翻拍告警"),
    OBSTRUCTION_WITHIN_BOX(-9105L, "身份证框内遮挡告警"),
    TEMPORARY_ID_CARD_WARNING(-9104L, "临时身份证告警"),
    SUSPECTED_PS_MARKS(-9106L, "身份证疑似存在PS痕迹告警"),
    GLARE_WARNING(-9107L, "身份证反光告警");

    private final Long code;
    private final String msg;


    public static WarnInfoEnum getWarnInfoEnum(Long code) {
        return Arrays.stream(WarnInfoEnum.values())
                .filter(warnInfoEnum -> warnInfoEnum.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }


}
