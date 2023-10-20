package com.matrix.auth.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.enums.DeviceTypeEnum;
import com.matrix.common.enums.RoleEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.enums.UserTypeEnum;
import com.matrix.common.model.RoleDTO;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.result.R;
import com.matrix.common.util.servlet.ServletUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import one.util.streamex.StreamEx;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {


    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceTypeEnum deviceTypeEnum) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(CommonConstants.LOGIN_USER_KEY, loginUser);
        storage.set(CommonConstants.USER_KEY, loginUser.getUserId());
        storage.set(CommonConstants.TENANT_KEY, loginUser.getTenantId());
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceTypeEnum)) {
            model.setDevice(deviceTypeEnum.getDevice());
        }
        StpUtil.login(loginUser.obtainedLoginId(),
                model.setExtra(CommonConstants.USER_KEY, loginUser.getUserId())
                        .setExtra(CommonConstants.TENANT_KEY, loginUser.getTenantId())
        );
        StpUtil.getSession().set(CommonConstants.LOGIN_USER_KEY, loginUser);
    }


    public static void loginByOpenId(String openId, LoginUser loginUser, DeviceTypeEnum deviceTypeEnum) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(CommonConstants.LOGIN_USER_KEY, loginUser);
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceTypeEnum)) {
            model.setDevice(deviceTypeEnum.getDevice());
        }
        model.setTimeout(Duration.ofMinutes(5).toSeconds());
        StpUtil.login("openId:" + openId, model);
        StpUtil.getSession().set(CommonConstants.LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static <U extends LoginUser> U getLoginUser() {
        U loginUser = (U) SaHolder.getStorage().get(CommonConstants.LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        SaSession session = StpUtil.getSession();
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        loginUser = (U) session.get(CommonConstants.LOGIN_USER_KEY);
        SaHolder.getStorage().set(CommonConstants.LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取用户基于token
     */
    public static <U extends LoginUser> U getLoginUser(String token) {
        Object loginId = StpUtil.getLoginIdByToken(token);
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        return (U) session.get(CommonConstants.LOGIN_USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(CommonConstants.USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(StpUtil.getExtra(CommonConstants.USER_KEY));
                SaHolder.getStorage().set(CommonConstants.USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    public static Long getTenantId() {
        Long tenantId;
        try {
            tenantId = Convert.toLong(SaHolder.getStorage().get(CommonConstants.TENANT_KEY));
            if (ObjectUtil.isNull(tenantId)) {
                tenantId = Convert.toLong(StpUtil.getExtra(CommonConstants.TENANT_KEY));
                SaHolder.getStorage().set(CommonConstants.TENANT_KEY, tenantId);
            }
        } catch (Exception e) {
            return null;
        }
        return tenantId;
    }

    public static Long getUserIdByUserType() {
        if (StpUtil.isLogin()) {
            return getUserId();
        } else {
            return ServletUtils.getUserIdByRequestHead();
        }
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户类型
     */
    public static UserTypeEnum getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserTypeEnum.getUserType(loginId);
    }


    /**
     * 检查租户是否认证
     *
     * @return boolean
     */

    public static boolean checkTenantAuth() {
        final LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        final Long tenantId = loginUser.getTenantId();
        final Long shopId = loginUser.getShopId();
        if (tenantId == null || tenantId == 0L) {
            R.throwFail(SystemErrorTypeEnum.TENANT_NOT_AUTH);
        }

        final List<RoleDTO> roles = loginUser.getRoles();
        if (CollUtil.isNotEmpty(roles)) {
            final boolean anyMatch = StreamEx.of(roles).anyMatch(roleDTO -> Objects
                    .equals(roleDTO.getRoleKey(), RoleEnum.SHOP_MANAGER.getRoleKey()));
            if (anyMatch) {
                return true;
            }
        }

        if (shopId == null || shopId == 0L) {
            R.throwFail(SystemErrorTypeEnum.SHOP_NOT_FOUND);
        }
        return true;
    }

    public static void logout(Long userId) {
        final List<String> deviceType = Arrays.stream(DeviceTypeEnum.values()).map(DeviceTypeEnum::getDevice).toList();
        deviceType.forEach(device -> {
            final String loginId = LoginUser.getLoginId(device, userId);
            StpUtil.logout(loginId);
        });
    }
}
