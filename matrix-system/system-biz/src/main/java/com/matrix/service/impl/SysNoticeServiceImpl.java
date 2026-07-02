package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysNotice;
import com.matrix.mapper.SysNoticeMapper;
import com.matrix.service.SysNoticeService;
import org.springframework.stereotype.Service;

/**
 * SysNotice 服务实现。
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
}
