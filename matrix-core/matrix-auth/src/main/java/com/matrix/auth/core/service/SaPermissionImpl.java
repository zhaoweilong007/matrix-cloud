package com.matrix.auth.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.matrix.auth.utils.LoginHelper;
import com.matrix.common.enums.UserTypeEnum;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.model.login.PcLoginUser;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserType(loginUser.getDeviceType());
        if (userTypeEnum == UserTypeEnum.SYS_USER) {
            final PcLoginUser pcLoginUser = (PcLoginUser) loginUser;
            return new ArrayList<>(pcLoginUser.getMenuPermission());
        } else if (userTypeEnum == UserTypeEnum.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserType(loginUser.getDeviceType());
        if (userTypeEnum == UserTypeEnum.SYS_USER) {
            final PcLoginUser pcLoginUser = (PcLoginUser) loginUser;
            return new ArrayList<>(pcLoginUser.getMenuPermission());
        } else if (userTypeEnum == UserTypeEnum.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return new ArrayList<>();
    }
}
