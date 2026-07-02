package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysSocial;
import com.matrix.mapper.SysSocialMapper;
import com.matrix.service.SysSocialService;
import org.springframework.stereotype.Service;

@Service
public class SysSocialServiceImpl extends ServiceImpl<SysSocialMapper, SysSocial> implements SysSocialService {}
