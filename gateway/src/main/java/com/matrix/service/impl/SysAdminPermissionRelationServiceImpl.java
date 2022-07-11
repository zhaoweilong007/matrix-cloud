package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysAdminPermissionRelationMapper;
import com.matrix.entity.po.SysAdminPermissionRelation;
import com.matrix.service.SysAdminPermissionRelationService;
import org.springframework.stereotype.Service;

/**
 * (SysAdminPermissionRelation)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Service("sysAdminPermissionRelationService")
public class SysAdminPermissionRelationServiceImpl extends ServiceImpl<SysAdminPermissionRelationMapper, SysAdminPermissionRelation> implements SysAdminPermissionRelationService {

}

