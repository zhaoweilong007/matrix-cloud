package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysOperateLog;
import com.matrix.mapper.SysOperateLogMapper;
import com.matrix.service.SysOperateLogService;
import org.springframework.stereotype.Service;

/**
 * SysOperateLog 服务实现。
 */
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements SysOperateLogService {
}
