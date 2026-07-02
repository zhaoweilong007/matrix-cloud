package com.matrix.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.dto.SysAdminLoginDto;
import com.matrix.api.system.entity.dto.SysAdminRegisterDto;
import com.matrix.api.system.entity.dto.UpdateAdminPasswordDto;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.api.system.entity.vo.SysAdminUserInfo;
import com.matrix.common.enums.DeviceTypeEnum;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.system.mapper.SysAdminMapper;
import com.matrix.auth.core.PasswordLockoutService;
import com.matrix.auth.utils.LoginHelper;
import com.matrix.system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * (SysAdmin)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Service
@RequiredArgsConstructor
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService, LoginService {

    private final SysMenuService sysMenuService;
    private final SysRoleService sysRoleService;
    private final SysResourceService sysResourceService;
    private final PasswordLockoutService passwordLockoutService;


    @Override
    public SysAdmin getAdminByUsername(String username) {
        return this.getOne(Wrappers.<SysAdmin>query().lambda().eq(SysAdmin::getUsername, username));
    }

    @Override
    public String register(SysAdminRegisterDto sysAdminRegisterDto) {
        boolean exists = baseMapper.exists(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminRegisterDto.getUsername()));
        Assert.isTrue(!exists, () -> new ServiceException(BusinessErrorTypeEnum.USER_EXIST));
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setUsername(sysAdminRegisterDto.getUsername());
        sysAdmin.setPassword(SaSecureUtil.md5(sysAdminRegisterDto.getPassword()));
        sysAdmin.setEmail(sysAdminRegisterDto.getEmail());
        baseMapper.insert(sysAdmin);
        return sysAdmin.getUsername();
    }

    @Override
    public SaTokenInfo login(SysAdminLoginDto sysAdminLoginDto) {
        SysAdmin sysAdmin = baseMapper.selectOne(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminLoginDto.getUsername()));
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST));

        // 检查账户锁定
        passwordLockoutService.checkLocked(sysAdminLoginDto.getUsername());

        // 验证密码
        if (!sysAdmin.getPassword().equals(SaSecureUtil.md5(sysAdminLoginDto.getPassword()))) {
            passwordLockoutService.recordPasswordError(sysAdminLoginDto.getUsername());
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }

        // 密码正确，清除错误计数
        passwordLockoutService.clearErrorCount(sysAdminLoginDto.getUsername());

        LoginUser loginUser = buildLoginUser(sysAdmin);
        LoginHelper.loginByDevice(loginUser, DeviceTypeEnum.PC);
        sysAdmin.setLoginTime(new Date());
        updateById(sysAdmin);
        return StpUtil.getTokenInfo();
    }

    private LoginUser buildLoginUser(SysAdmin sysAdmin) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysAdmin.getId());
        loginUser.setUsername(sysAdmin.getUsername());
        loginUser.setTenantId(sysAdmin.getTenantId());
        return loginUser;
    }

    @Override
    public String refreshToken(String oldToken) {
        return null;
    }

    @Override
    public SysAdminUserInfo userInfo(Long id) {
        SysAdmin sysAdmin = baseMapper.selectById(id);
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST));
        SysAdminUserInfo sysAdminUserInfo = new SysAdminUserInfo();
        sysAdminUserInfo.setSysAdmin(sysAdmin);
        sysAdminUserInfo.setMenus(sysMenuService.getMenuByAdminId(id));
        sysAdminUserInfo.setRoles(sysRoleService.getRoleByAdminId(id));
        return sysAdminUserInfo;
    }

    @Override
    public Boolean updateHidden(Long id, Integer status) {
        SysAdmin sysAdmin = getById(id);
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST));
        sysAdmin.setStatus(status);
        return updateById(sysAdmin);
    }

    @Override
    public List<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public Boolean updatePassword(UpdateAdminPasswordDto updatePasswordParam) {
        //更改密码
        SysAdmin sysAdmin = getById(updatePasswordParam.getId());
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST));
        if (!Objects.equals(SaSecureUtil.md5(updatePasswordParam.getOldPassword()), sysAdmin.getPassword())) {
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }
        sysAdmin.setPassword(SaSecureUtil.md5(updatePasswordParam.getNewPassword()));
        return updateById(sysAdmin);
    }


}

