package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysMessage;
import com.matrix.mapper.SysMessageMapper;
import com.matrix.service.SysMessageService;
import org.springframework.stereotype.Service;

@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {}
