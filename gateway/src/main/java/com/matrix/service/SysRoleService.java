package com.matrix.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.entity.dto.SysRoleDto;
import com.matrix.entity.po.SysRole;

import java.util.List;

/**
 * (SysRole)表服务接口
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
public interface SysRoleService extends IService<SysRole> {

    Boolean add(SysRoleDto sysRoleDto);

    Boolean update(SysRoleDto sysRoleDto);



    Boolean assignRole(Long userId,List<Long> roleIds);


    /**
     * 获取角色列表
     * @param id
     * @return
     */
    List<SysRole> getRoleByAdminId(Long id);

    Boolean updateHidden(Long id, Integer status);
}

