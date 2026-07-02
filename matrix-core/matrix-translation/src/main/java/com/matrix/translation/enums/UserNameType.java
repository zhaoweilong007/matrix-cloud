package com.matrix.translation.enums;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户名称类型枚举，定义不同版本用户的缓存键格式
 */
@Getter
@RequiredArgsConstructor
public enum UserNameType {

    /**
     * 新版本用户
     */
    SYS_USER("sys_user", "matrix_sys_user_id:%s#30d");

    private final String type;
    private final String key;

    /**
     * 根据用户类型查找对应的枚举
     *
     * @param other 用户类型字符串
     * @return UserNameType 枚举，未找到返回 null
     */
    public static UserNameType valueOfByType(String other) {
        return Arrays.stream(values())
                .filter(userNameType -> Objects.equals(other, userNameType.getType()))
                .findFirst()
                .orElse(null);
    }
}
