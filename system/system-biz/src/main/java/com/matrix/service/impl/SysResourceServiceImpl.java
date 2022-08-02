package com.matrix.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.dto.SysResourceDto;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.api.system.entity.po.SysRoleResourceRelation;
import com.matrix.convert.ConvertMapper;
import com.matrix.mapper.SysResourceMapper;
import com.matrix.service.SysResourceService;
import com.matrix.service.SysRoleResourceRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysResource)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Service
@RequiredArgsConstructor
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    private final SysRoleResourceRelationService sysRoleResourceRelationService;


    @Override
    @CachePut(value = "resource", key = "'resource'")
    public Boolean assignResource(Long roleId, List<Long> resourceIds) {
        sysRoleResourceRelationService.remove(new LambdaQueryWrapper<SysRoleResourceRelation>().eq(SysRoleResourceRelation::getRoleId, roleId));
        List<SysRoleResourceRelation> list = resourceIds.stream().filter(id -> sysRoleResourceRelationService.getById(id) == null).map(rid -> {
            SysRoleResourceRelation sysRoleResourceRelation = new SysRoleResourceRelation();
            sysRoleResourceRelation.setRoleId(roleId);
            sysRoleResourceRelation.setResourceId(rid);
            return sysRoleResourceRelation;
        }).collect(Collectors.toList());
        return sysRoleResourceRelationService.saveBatch(list);
    }

    @Override
    public Boolean create(SysResourceDto sysResourceDto) {
        SysResource sysResource = ConvertMapper.INSTALL.convert(sysResourceDto);
        return this.save(sysResource);
    }

    @Override
    public Boolean update(SysResourceDto sysResourceDto) {
        SysResource sysResource = ConvertMapper.INSTALL.convert(sysResourceDto);
        return this.updateById(sysResource);
    }

    @Cacheable(value = "resource", key = "'resource'")
    @Override
    public List<SysResource> getResourceByAdminId(Long adminId) {
        return baseMapper.getResourceByAdminId(adminId);
    }

    @Override
    @Cacheable(value = "resource", key = "'resource'")
    public List<SysResource> getResourceByRoleId(Long id) {
        return baseMapper.getResourceByRoleId(id);
    }

    @Override
    public List<String> getAllResource() {
        return baseMapper.getAllResource();
    }
}

