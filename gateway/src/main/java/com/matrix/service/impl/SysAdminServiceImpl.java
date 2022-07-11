package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysAdminMapper;
import com.matrix.entity.po.SysAdmin;
import com.matrix.service.SysAdminService;
import org.springframework.stereotype.Service;

/**
 * (SysAdmin)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Service("sysAdminService")
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

}

