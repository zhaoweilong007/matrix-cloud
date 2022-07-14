package com.matrix.security;

import cn.dev33.satoken.stp.StpInterface;
import com.matrix.entity.po.SysResource;
import com.matrix.entity.po.SysRole;
import com.matrix.service.SysResourceService;
import com.matrix.service.SysRoleService;
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
    private SysRoleService roleService;
    @Autowired
    private SysResourceService sysResourceService;

    /**
     * 获取权限列表
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("StpInterfaceImpl getPermissionList loginId:{},loginType:{}", loginId, loginType);
        List<SysResource> resources = sysResourceService.getResourceByAdminId(Long.valueOf(loginId.toString()));
        return resources.stream().map(SysResource::getUrl).collect(Collectors.toList());
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
        List<SysRole> roles = roleService.getRoleByAdminId(((Long) loginId));
        return roles.stream().map(SysRole::getName).collect(Collectors.toList());
    }
}
