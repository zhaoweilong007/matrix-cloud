package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysPost;
import com.matrix.mapper.SysPostMapper;
import com.matrix.service.SysPostService;
import org.springframework.stereotype.Service;

/**
 * SysPost 服务实现。
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {
}
