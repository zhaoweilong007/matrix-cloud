package com.matrix.security;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：鉴权接口
 *
 * @author zwl
 * @since 2022/7/8 14:46
 **/
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {


    /**
     * 获取权限列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    /**
     * 获取角色列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
