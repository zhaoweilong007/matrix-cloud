package com.matrix.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.api.system.entity.dto.SysResourceDto;
import com.matrix.api.system.entity.po.SysResource;

import java.util.List;

/**
 * (SysResource)表服务接口
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
public interface SysResourceService extends IService<SysResource> {
    /**
     * 分配资源
     *
     * @param roleId      权限id
     * @param resourceIds 资源ids
     * @return
     */
    Boolean assignResource(Long roleId, List<Long> resourceIds);

    Boolean create(SysResourceDto sysResourceDto);

    Boolean update(SysResourceDto sysResourceDto);

    List<SysResource> getResourceByAdminId(Long adminId);

    List<SysResource> getResourceByRoleId(Long id);

    List<String> getAllResource();

}

