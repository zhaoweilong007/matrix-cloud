package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysConfig;
import com.matrix.mapper.SysConfigMapper;
import com.matrix.service.SysConfigService;
import org.springframework.stereotype.Service;

/**
 * SysConfig 服务实现。
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
}
