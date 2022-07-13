package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.convert.ConvertMapper;
import com.matrix.entity.dto.SysRoleDto;
import com.matrix.entity.po.SysRole;
import com.matrix.entity.po.SysRoleMenuRelation;
import com.matrix.mapper.SysRoleMapper;
import com.matrix.service.SysMenuService;
import com.matrix.service.SysRoleMenuRelationService;
import com.matrix.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysRole)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysMenuService sysMenuService;

    private final SysRoleMenuRelationService sysRoleMenuRelationService;

    @Override
    public Boolean add(SysRoleDto sysRoleDto) {
        SysRole sysRole = ConvertMapper.INSTALL.convert(sysRoleDto);
        return this.save(sysRole);
    }

    @Override
    public Boolean update(SysRoleDto sysRoleDto) {
        SysRole sysRole = ConvertMapper.INSTALL.convert(sysRoleDto);
        return this.updateById(sysRole);
    }

    @Override
    public Boolean assignMenu(Long roleId, List<Long> menuIds) {
        List<SysRoleMenuRelation> relationList = menuIds.stream().filter(id -> sysMenuService.getById(id) == null).map(mid -> {
            SysRoleMenuRelation sysRoleMenuRelation = new SysRoleMenuRelation();
            sysRoleMenuRelation.setRoleId(roleId);
            sysRoleMenuRelation.setMenuId(mid);
            return sysRoleMenuRelation;
        }).collect(Collectors.toList());

        return sysRoleMenuRelationService.saveBatch(relationList);
    }

    @Override
    public Boolean assignResource(Long roleId, List<Long> resourceIds) {
        return null;
    }


}

