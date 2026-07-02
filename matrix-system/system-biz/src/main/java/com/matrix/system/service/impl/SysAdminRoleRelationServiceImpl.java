package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysAdminRoleRelation;
import com.matrix.system.mapper.SysAdminRoleRelationMapper;
import com.matrix.system.service.SysAdminRoleRelationService;
import org.springframework.stereotype.Service;

/**
 * (SysAdminRoleRelation)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Service("sysAdminRoleRelationService")
public class SysAdminRoleRelationServiceImpl extends ServiceImpl<SysAdminRoleRelationMapper, SysAdminRoleRelation> implements SysAdminRoleRelationService {

}

