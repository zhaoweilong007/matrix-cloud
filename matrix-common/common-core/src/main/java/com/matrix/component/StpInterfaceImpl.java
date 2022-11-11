package com.matrix.component;

import cn.dev33.satoken.stp.StpInterface;
import com.matrix.entity.vo.LoginUser;
import com.matrix.utils.LoginHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 描述：获取权限角色
 *
 * @author zwl
 * @since 2022/7/8 14:46
 **/
@Slf4j
public class StpInterfaceImpl implements StpInterface {


    /**
     * 获取权限列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("StpInterfaceImpl getPermissionList loginId:{},loginType:{}", loginId, loginType);
        LoginUser loginUser = LoginHelper.getLoginUser();
        return loginUser.getPermissions();
    }

    /**
     * 获取角色列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("StpInterfaceImpl getRoleList loginId:{},loginType:{}", loginId, loginType);
        LoginUser loginUser = LoginHelper.getLoginUser();
        return loginUser.getRoles();
    }
}
