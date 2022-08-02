package com.matrix.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.api.system.entity.po.Tenant;
import com.matrix.mapper.SysAdminMapper;
import com.matrix.mapper.TenantMapper;
import com.matrix.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/27 15:17
 **/
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Override
    public Boolean assignUser(List<Long> userIds, Long tenantId) {
        boolean exists = baseMapper.exists(Wrappers.<Tenant>lambdaQuery().eq(Tenant::getId, tenantId));
        Assert.isFalse(exists, "租户不存在");
        LambdaUpdateWrapper<SysAdmin> updateWrapper = Wrappers.<SysAdmin>lambdaUpdate().in(SysAdmin::getId, userIds).set(SysAdmin::getTenantId, tenantId);
        return sysAdminMapper.update(null, updateWrapper) > 0;
    }
}
