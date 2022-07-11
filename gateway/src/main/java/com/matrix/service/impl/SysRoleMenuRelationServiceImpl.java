package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysRoleMenuRelationMapper;
import com.matrix.entity.po.SysRoleMenuRelation;
import com.matrix.service.SysRoleMenuRelationService;
import org.springframework.stereotype.Service;

/**
 * (SysRoleMenuRelation)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Service("sysRoleMenuRelationService")
public class SysRoleMenuRelationServiceImpl extends ServiceImpl<SysRoleMenuRelationMapper, SysRoleMenuRelation> implements SysRoleMenuRelationService {

}

