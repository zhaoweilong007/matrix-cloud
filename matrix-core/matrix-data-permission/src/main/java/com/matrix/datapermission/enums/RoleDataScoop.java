package com.matrix.datapermission.enums;

import com.matrix.common.enums.RoleEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;

import java.util.Objects;

/**
 * 角色数据权限
 *
 * @author ZhaoWeiLong
 * @since 2023/8/28
 **/
@Getter
@RequiredArgsConstructor
public enum RoleDataScoop {

    /**
     * 管理员数据权限
     */
    admin(RoleEnum.ADMIN.getRoleKey(), DataScopeType.ALL),
    /**
     * 店东数据权限
     */
    SHOP_OWNER(RoleEnum.SHOP_OWNER.getRoleKey(), DataScopeType.ALL),
    /**
     * 店长数据权限
     */
    SHOP_MANAGER(RoleEnum.SHOP_MANAGER.getRoleKey(), DataScopeType.SHOP),

    /**
     * 分店店长数据权限
     */
    BRANCH_SHOP_MANAGER(RoleEnum.BRANCH_SHOP_MANAGER.getRoleKey(), DataScopeType.BRANCH_SHOP),

    /**
     * 经纪人数据权限
     */
    BROKER(RoleEnum.BROKER.getRoleKey(), DataScopeType.SELF);


    private final String roleKey;

    private final DataScopeType dataScope;


    public static RoleDataScoop ofRoleKey(String roleKey) {
        return StreamEx.of(values()).findFirst(roleDataScoop -> Objects.equals(roleDataScoop.getRoleKey(), roleKey)).orElse(null);
    }
}
