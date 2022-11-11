package com.matrix.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.api.system.entity.po.SysResource;

import java.util.List;

/**
 * (SysResource)表数据库访问层
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> getResourceByAdminId(Long adminId);

    List<SysResource> getResourceByRoleId(Long roleId);

    List<String> getAllResource();
}

