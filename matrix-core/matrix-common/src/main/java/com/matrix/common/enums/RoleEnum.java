package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统默认橘色
 *
 * @author ZhaoWeiLong
 * @since 2023/7/20
 **/
@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    ADMIN(1, "admin", "超级管理员"),
    SHOP_OWNER(2, "shop_owner", "店东"),
    SHOP_MANAGER(3, "shop_manager", "店长"),
    BRANCH_SHOP_MANAGER(4, "branch_shop_manager", "分店店长"),
    BROKER(5, "broker", "经纪人");

    private final Integer value;

    private final String roleKey;

    private final String roleName;


    public static RoleEnum ofRoleKey(String roleKey) {
        return StreamEx.of(values()).findFirst(roleEnum -> Objects.equals(roleEnum.getRoleKey(), roleKey)).orElse(null);
    }


    public static List<String> sortByRole(RoleEnum roleEnum) {
        return StreamEx.of(values()).filter(r -> roleEnum.getValue() >= r.getValue()).map(RoleEnum::getRoleKey).collect(Collectors.toList());
    }
}
