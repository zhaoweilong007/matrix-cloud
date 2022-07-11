package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysResourceMapper;
import com.matrix.entity.po.SysResource;
import com.matrix.service.SysResourceService;
import org.springframework.stereotype.Service;

/**
 * (SysResource)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Service("sysResourceService")
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

}

