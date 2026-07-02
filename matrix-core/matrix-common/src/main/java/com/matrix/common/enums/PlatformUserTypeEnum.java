package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 **/
@Getter
@RequiredArgsConstructor
public enum PlatformUserTypeEnum {
    AUDIT_USER(1, "S端后台用户"),
    USER_BASIC(2, "C端用户用户"),
    SYS_USER(3, "B端用户");

    private final Integer value;

    private final String desc;
}
