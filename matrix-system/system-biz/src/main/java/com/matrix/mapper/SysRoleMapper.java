package com.matrix.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.api.system.entity.po.SysRole;

import java.util.List;

/**
 * (SysRole)表数据库访问层
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getRoleByAdminId(Long adminId);

}

