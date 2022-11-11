package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysRoleResourceRelation;
import com.matrix.mapper.SysRoleResourceRelationMapper;
import com.matrix.service.SysRoleResourceRelationService;
import org.springframework.stereotype.Service;

/**
 * (SysRoleResourceRelation)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:53
 */
@Service("sysRoleResourceRelationService")
public class SysRoleResourceRelationServiceImpl extends ServiceImpl<SysRoleResourceRelationMapper, SysRoleResourceRelation> implements SysRoleResourceRelationService {

}

