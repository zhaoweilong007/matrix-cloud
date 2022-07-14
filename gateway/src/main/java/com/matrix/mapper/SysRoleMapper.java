package com.matrix.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.entity.po.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysRole)表数据库访问层
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getRoleByAdminId(@Param("id") Long id);

}

