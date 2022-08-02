package com.matrix.security;

import cn.dev33.satoken.stp.StpInterface;
import com.matrix.api.system.ResourceAPI;
import com.matrix.api.system.RoleAPI;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.entity.vo.Result;
import com.matrix.exception.ServiceException;
import com.matrix.utils.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：获取权限角色
 *
 * @author zwl
 * @since 2022/7/8 14:46
 **/
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RoleAPI roleAPI;
    @Autowired
    private ResourceAPI resourceAPI;

    /**
     * 获取权限列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("StpInterfaceImpl getPermissionList loginId:{},loginType:{}", loginId, loginType);
        Long userId = LoginHelper.getUserId();
        Result<List<SysResource>> result = resourceAPI.getResourceByAdminId(userId);
        if (result.isSuccess()) {
            return result.getData().stream().map(SysResource::getUrl).collect(Collectors.toList());
        }
        throw new ServiceException(result);
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
        Long userId = LoginHelper.getUserId();
        Result<List<SysRole>> result = roleAPI.getRoleByAdminId(userId);
        if (result.isFail()) {
            throw new ServiceException(result);
        }
        return result.getData().stream().map(SysRole::getName).collect(Collectors.toList());
    }
}
