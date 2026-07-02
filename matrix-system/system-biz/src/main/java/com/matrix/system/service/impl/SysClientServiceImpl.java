package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysClient;
import com.matrix.system.mapper.SysClientMapper;
import com.matrix.system.service.SysClientService;
import org.springframework.stereotype.Service;

@Service
public class SysClientServiceImpl extends ServiceImpl<SysClientMapper, SysClient> implements SysClientService {}
