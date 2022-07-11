package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysPermissionMapper;
import com.matrix.entity.po.SysPermission;
import com.matrix.service.SysPermissionService;
import org.springframework.stereotype.Service;

/**
 * (SysPermission)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Service("sysPermissionService")
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

}

