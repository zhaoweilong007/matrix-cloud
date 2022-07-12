package com.matrix.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.entity.dto.UpdateAdminPasswordDto;
import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.po.SysResource;
import com.matrix.entity.po.SysRole;
import com.matrix.exception.model.BusinessErrorType;
import com.matrix.exception.model.ServiceException;
import com.matrix.mapper.SysAdminMapper;
import com.matrix.service.SysAdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (SysAdmin)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    @Override
    public SysAdmin getAdminByUsername(String username) {
        return null;
    }

    @Override
    public SysAdmin register(SysAdminRegisterDto sysAdminRegisterDto) {
        Long count = baseMapper.selectCount(this.lambdaQuery().eq(SysAdmin::getUsername, sysAdminRegisterDto.getUserName()));
        if (count > 0) {
            return null;
        }
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setUsername(sysAdminRegisterDto.getUserName());
        sysAdmin.setPassword(SaSecureUtil.md5(sysAdminRegisterDto.getPassword()));
        sysAdmin.setEmail(sysAdminRegisterDto.getEmail());
        baseMapper.insert(sysAdmin);
        return sysAdmin;
    }

    @Override
    public SaTokenInfo login(String username, String password) {
        SysAdmin sysAdmin = baseMapper.selectOne(this.lambdaQuery().eq(SysAdmin::getUsername, username));
        if (sysAdmin == null) {
            throw new ServiceException(BusinessErrorType.User_not_found);
        }
        if (!Objects.equals(SaSecureUtil.md5(password), sysAdmin.getPassword())) {
            throw new ServiceException(BusinessErrorType.Authentication_failed);
        }
        StpUtil.login(sysAdmin.getId());
        return StpUtil.getTokenInfo();
    }

    @Override
    public String refreshToken(String oldToken) {
        return null;
    }

    @Override
    public SysAdmin getItem(Long id) {
        return null;
    }

    @Override
    public List<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public int update(Long id, SysAdmin admin) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        return 0;
    }

    @Override
    public List<SysRole> getRoleList(Long adminId) {
        return null;
    }

    @Override
    public List<SysResource> getResourceList(Long adminId) {
        return null;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordDto updatePasswordParam) {
        return 0;
    }
}

