package com.matrix.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.entity.po.Tenant;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/27 15:19
 **/
public interface TenantService extends IService<Tenant> {
    Boolean assignUser(List<Long> userIds, Long tenantId);
}
