package com.matrix.translation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/12
 **/
@Getter
@RequiredArgsConstructor
public enum UserNameType {

    /**
     * 新版本用户
     */
    SYS_USER("sys_user", "matrix_sys_user_id:%s#30d");


    private final String type;
    private final String key;

    public static UserNameType valueOfByType(String other) {
        return Arrays.stream(values()).filter(userNameType -> Objects.equals(other, userNameType.getType())).findFirst().orElse(null);
    }
}
