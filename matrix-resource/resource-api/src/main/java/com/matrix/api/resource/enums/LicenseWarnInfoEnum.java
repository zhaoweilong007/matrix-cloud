package com.matrix.api.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author LeonZhou
 * @since 2024/05/14
 **/
@Getter
@AllArgsConstructor
public enum LicenseWarnInfoEnum {

    PHOTOCOPY_WARNING(-9102L, "营业执照复印件告警"),
    REPHOTOGRAPH_WARNING(-9103L, "营业执照翻拍告警");

    private final Long code;
    private final String msg;


    public static LicenseWarnInfoEnum getWarnInfoEnum(Long code) {
        return Arrays.stream(LicenseWarnInfoEnum.values())
                .filter(warnInfoEnum -> warnInfoEnum.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }


}
