package com.matrix.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.matrix.common.util.spring.SpringUtils;
import com.matrix.log.event.LogininforEvent;
import com.matrix.prometheus.annotation.BizTrace;
import com.matrix.system.mapper.SysAdminMapper;
import com.matrix.auth.core.PasswordLockoutService;
import com.matrix.auth.utils.LoginHelper;
import com.matrix.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.crypto.digest.BCrypt;
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
    @BizTrace(id = "#sysAdminRegisterDto.username", type = "USER_REGISTER")
    public String register(SysAdminRegisterDto sysAdminRegisterDto) {
        boolean exists = baseMapper.exists(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminRegisterDto.getUsername()));
        Assert.isTrue(!exists, () -> new ServiceException(BusinessErrorTypeEnum.USER_EXIST));
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setUsername(sysAdminRegisterDto.getUsername());
        sysAdmin.setPassword(BCrypt.hashpw(sysAdminRegisterDto.getPassword()));
        sysAdmin.setEmail(sysAdminRegisterDto.getEmail());
        baseMapper.insert(sysAdmin);
        return sysAdmin.getUsername();
    }

    @Override
    @BizTrace(id = "#sysAdminLoginDto.username", type = "USER_LOGIN")
    public SaTokenInfo login(SysAdminLoginDto sysAdminLoginDto) {
        SysAdmin sysAdmin = baseMapper.selectOne(Wrappers.<SysAdmin>lambdaQuery().eq(SysAdmin::getUsername, sysAdminLoginDto.getUsername()));
        if (sysAdmin == null) {
            publishLoginEvent(sysAdminLoginDto.getUsername(), "1", "用户不存在");
            throw new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST);
        }

        // 检查账户锁定
        try {
            passwordLockoutService.checkLocked(sysAdminLoginDto.getUsername());
        } catch (ServiceException e) {
            publishLoginEvent(sysAdminLoginDto.getUsername(), "1", "账户已锁定");
            throw e;
        }

        // 验证密码：优先 BCrypt，兼容旧 MD5 密码并自动升级
        if (!verifyAndUpgradePassword(sysAdmin, sysAdminLoginDto.getPassword())) {
            passwordLockoutService.recordPasswordError(sysAdminLoginDto.getUsername());
            publishLoginEvent(sysAdminLoginDto.getUsername(), "1", "密码错误");
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }

        // 密码正确，清除错误计数
        passwordLockoutService.clearErrorCount(sysAdminLoginDto.getUsername());

        LoginUser loginUser = buildLoginUser(sysAdmin);
        LoginHelper.loginByDevice(loginUser, DeviceTypeEnum.PC);
        sysAdmin.setLoginTime(new Date());
        updateById(sysAdmin);

        publishLoginEvent(sysAdminLoginDto.getUsername(), "0", "登录成功");
        return StpUtil.getTokenInfo();
    }

    /**
     * 验证密码，兼容 MD5 旧密码并自动升级为 BCrypt。
     *
     * @param sysAdmin   用户实体
     * @param rawPassword 明文密码
     * @return 密码是否正确
     */
    private boolean verifyAndUpgradePassword(SysAdmin sysAdmin, String rawPassword) {
        String stored = sysAdmin.getPassword();
        // BCrypt 密文以 $2a$ 开头
        if (stored.startsWith("$2a$")) {
            return BCrypt.checkpw(rawPassword, stored);
        }
        // 兼容旧 MD5 密码
        if (stored.equals(SaSecureUtil.md5(rawPassword))) {
            // 自动升级为 BCrypt
            sysAdmin.setPassword(BCrypt.hashpw(rawPassword));
            updateById(sysAdmin);
            return true;
        }
        return false;
    }

    /**
     * 发布登录日志事件。
     *
     * @param username 用户名
     * @param status   状态：0-成功 1-失败
     * @param msg      消息
     */
    private void publishLoginEvent(String username, String status, String msg) {
        LogininforEvent event = new LogininforEvent();
        event.setType(1);
        event.setUserName(username);
        event.setStatus(status);
        event.setMsg(msg);
        SpringUtils.context().publishEvent(event);
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
        Object loginId = StpUtil.getLoginIdByToken(oldToken);
        if (loginId == null) {
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }
        StpUtil.login(loginId);
        return StpUtil.getTokenValue();
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
        Page<SysAdmin> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysAdmin> wrapper = Wrappers.<SysAdmin>lambdaQuery()
                .like(StrUtil.isNotBlank(keyword), SysAdmin::getUsername, keyword);
        wrapper.or(StrUtil.isNotBlank(keyword), w -> w.like(SysAdmin::getEmail, keyword));
        return baseMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public Boolean updatePassword(UpdateAdminPasswordDto updatePasswordParam) {
        SysAdmin sysAdmin = getById(updatePasswordParam.getId());
        Assert.notNull(sysAdmin, () -> new ServiceException(BusinessErrorTypeEnum.USER_NOT_EXIST));
        // 验证旧密码：优先 BCrypt，兼容 MD5
        String stored = sysAdmin.getPassword();
        boolean verified;
        if (stored.startsWith("$2a$")) {
            verified = BCrypt.checkpw(updatePasswordParam.getOldPassword(), stored);
        } else {
            verified = Objects.equals(SaSecureUtil.md5(updatePasswordParam.getOldPassword()), stored);
        }
        if (!verified) {
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }
        sysAdmin.setPassword(BCrypt.hashpw(updatePasswordParam.getNewPassword()));
        return updateById(sysAdmin);
    }


}

