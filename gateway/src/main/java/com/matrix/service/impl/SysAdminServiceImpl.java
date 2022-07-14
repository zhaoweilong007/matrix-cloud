package com.matrix.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.entity.dto.SysAdminLoginDto;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.entity.dto.UpdateAdminPasswordDto;
import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.vo.SysAdminUserInfo;
import com.matrix.exception.model.BusinessErrorType;
import com.matrix.exception.model.ServiceException;
import com.matrix.mapper.SysAdminMapper;
import com.matrix.service.LoginService;
import com.matrix.service.SysAdminService;
import com.matrix.service.SysMenuService;
import com.matrix.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService, LoginService {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public SysAdmin getAdminByUsername(String username) {
        return this.getOne(Wrappers.<SysAdmin>query().lambda().eq(SysAdmin::getUsername, username));
    }

    @Override
    public String register(SysAdminRegisterDto sysAdminRegisterDto) {
        Long count = baseMapper.selectCount(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminRegisterDto.getUsername()));
        if (count > 0) {
            throw new ServiceException(BusinessErrorType.USER_EXISTS);
        }
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
        if (sysAdmin == null) {
            throw new ServiceException(BusinessErrorType.USER_NOT_EXISTS);
        }
        if (!Objects.equals(SaSecureUtil.md5(sysAdminLoginDto.getPassword()), sysAdmin.getPassword())) {
            throw new ServiceException(BusinessErrorType.AUTHENTICATION_FAILED);
        }
        StpUtil.login(sysAdmin.getId());
        sysAdmin.setLoginTime(new Date());
        updateById(sysAdmin);
        return StpUtil.getTokenInfo();
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

