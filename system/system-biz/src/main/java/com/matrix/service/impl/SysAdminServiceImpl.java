package com.matrix.service.impl;

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
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.api.system.entity.vo.SysAdminUserInfo;
import com.matrix.entity.enums.DeviceType;
import com.matrix.entity.vo.LoginUser;
import com.matrix.exception.BusinessErrorType;
import com.matrix.exception.ServiceException;
import com.matrix.mapper.SysAdminMapper;
import com.matrix.service.*;
import com.matrix.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    @Override
    public SysAdmin getAdminByUsername(String username) {
        return this.getOne(Wrappers.<SysAdmin>query().lambda().eq(SysAdmin::getUsername, username));
    }

    @Override
    public String register(SysAdminRegisterDto sysAdminRegisterDto) {
        boolean exists = baseMapper.exists(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminRegisterDto.getUsername()));
        Assert.isTrue(!exists, () -> new ServiceException(BusinessErrorType.USER_EXISTS));
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
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorType.USER_NOT_EXISTS));
        Assert.notEquals(sysAdmin.getPassword(), SaSecureUtil.md5(sysAdminLoginDto.getPassword()), () -> new ServiceException(BusinessErrorType.PASSWORD_MISTAKE));
        LoginUser loginUser = buildLoginUser(sysAdmin);
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        sysAdmin.setLoginTime(new Date());
        updateById(sysAdmin);
        return StpUtil.getTokenInfo();
    }

    private LoginUser buildLoginUser(SysAdmin sysAdmin) {
        return LoginUser.builder().userId(sysAdmin.getId()).username(sysAdmin.getUsername())
                .tenantId(sysAdmin.getTenantId()).userType(sysAdmin.getUserType())
                .permissions(sysResourceService.getResourceByAdminId(sysAdmin.getId()).stream().map(SysResource::getUrl).collect(Collectors.toList()))
                .roles(sysRoleService.getRoleByAdminId(sysAdmin.getId()).stream().map(SysRole::getName).collect(Collectors.toList()))
                .build();
    }

    @Override
    public String refreshToken(String oldToken) {
        return null;
    }

    @Override
    public SysAdminUserInfo userInfo(Long id) {
        SysAdmin sysAdmin = baseMapper.selectById(id);
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorType.USER_NOT_EXISTS));
        SysAdminUserInfo sysAdminUserInfo = new SysAdminUserInfo();
        sysAdminUserInfo.setSysAdmin(sysAdmin);
        sysAdminUserInfo.setMenus(sysMenuService.getMenuByAdminId(id));
        sysAdminUserInfo.setRoles(sysRoleService.getRoleByAdminId(id));
        return sysAdminUserInfo;
    }

    @Override
    public Boolean updateHidden(Long id, Integer status) {
        SysAdmin sysAdmin = getById(id);
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorType.USER_NOT_EXISTS));
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
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorType.USER_NOT_EXISTS));
        if (!Objects.equals(SaSecureUtil.md5(updatePasswordParam.getOldPassword()), sysAdmin.getPassword())) {
            throw new ServiceException(BusinessErrorType.PASSWORD_MISTAKE);
        }
        sysAdmin.setPassword(SaSecureUtil.md5(updatePasswordParam.getNewPassword()));
        return updateById(sysAdmin);
    }


}

