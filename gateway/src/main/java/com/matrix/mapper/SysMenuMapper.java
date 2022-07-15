package com.matrix.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.entity.po.SysMenu;

import java.util.List;

/**
 * (SysMenu)表数据库访问层
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenuByAdminId(Long adminId);

    List<SysMenu> getMenuByRoleId(Long roleId);
}

