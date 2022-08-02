package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.dto.SysRoleDto;
import com.matrix.api.system.entity.po.SysAdminRoleRelation;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.convert.ConvertMapper;
import com.matrix.mapper.SysRoleMapper;
import com.matrix.service.SysAdminRoleRelationService;
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
    private final SysAdminRoleRelationService sysAdminRoleRelationService;

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
    public List<SysRole> getRoleByAdminId(Long id) {
        return this.baseMapper.getRoleByAdminId(id);
    }

    @Override
    public Boolean updateHidden(Long id, Integer status) {
        SysRole sysRole = getById(id);
        sysRole.setStatus(status);
        return this.updateById(sysRole);
    }

    @Override
    public Boolean assignRole(Long userId, List<Long> roleIds) {
        List<SysAdminRoleRelation> collect = roleIds.stream().filter(id -> getById(id) != null)
                .map(id -> {
                    SysAdminRoleRelation sysAdminRoleRelation = new SysAdminRoleRelation();
                    sysAdminRoleRelation.setAdminId(userId);
                    sysAdminRoleRelation.setRoleId(id);
                    return sysAdminRoleRelation;
                })
                .collect(Collectors.toList());
        return sysAdminRoleRelationService.saveBatch(collect);
    }

}

