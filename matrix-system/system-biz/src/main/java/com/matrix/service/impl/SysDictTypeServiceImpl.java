package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysDictType;
import com.matrix.mapper.SysDictTypeMapper;
import com.matrix.service.SysDictTypeService;
import org.springframework.stereotype.Service;

/**
 * SysDictType 服务实现。
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {
}
