package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysSocial;
import com.matrix.system.mapper.SysSocialMapper;
import com.matrix.system.service.SysSocialService;
import org.springframework.stereotype.Service;

@Service
public class SysSocialServiceImpl extends ServiceImpl<SysSocialMapper, SysSocial> implements SysSocialService {}
