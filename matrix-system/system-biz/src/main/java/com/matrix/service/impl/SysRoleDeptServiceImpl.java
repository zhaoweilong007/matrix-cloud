package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysRoleDept;
import com.matrix.mapper.SysRoleDeptMapper;
import com.matrix.service.SysRoleDeptService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements SysRoleDeptService {}
