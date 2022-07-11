package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysRolePermissionRelationMapper;
import com.matrix.entity.po.SysRolePermissionRelation;
import com.matrix.service.SysRolePermissionRelationService;
import org.springframework.stereotype.Service;

/**
 * (SysRolePermissionRelation)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Service("sysRolePermissionRelationService")
public class SysRolePermissionRelationServiceImpl extends ServiceImpl<SysRolePermissionRelationMapper, SysRolePermissionRelation> implements SysRolePermissionRelationService {

}

