package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysLoginLog;
import com.matrix.mapper.SysLoginLogMapper;
import com.matrix.service.SysLoginLogService;
import org.springframework.stereotype.Service;

/**
 * SysLoginLog 服务实现。
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
}
