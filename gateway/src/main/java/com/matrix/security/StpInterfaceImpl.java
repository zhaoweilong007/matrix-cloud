package com.matrix.security;

import cn.dev33.satoken.stp.StpInterface;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.client.SystemClient;
import com.matrix.entity.vo.Result;
import com.matrix.exception.ServiceException;
import com.matrix.exception.SystemErrorType;
import com.matrix.utils.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
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
    SystemClient systemClient;

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
        Result<List<SysResource>> result;
        try {
            result = systemClient.getResourceByAdminId(userId).get();
            if (result.isSuccess()) {
                return result.getData().stream().map(SysResource::getUrl).collect(Collectors.toList());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("StpInterfaceImpl getPermissionList error:{}", e.getMessage());
            throw new ServiceException(SystemErrorType.SYSTEM_BUSY);
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
        Result<List<SysRole>> result;
        try {
            result = systemClient.getRoleByAdminId(userId).get();
            if (result.isFail()) {
                throw new ServiceException(result);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("StpInterfaceImpl getRoleList error:{}", e.getMessage());
            throw new ServiceException(SystemErrorType.SYSTEM_BUSY);
        }
        return result.getData().stream().map(SysRole::getName).collect(Collectors.toList());
    }
}
