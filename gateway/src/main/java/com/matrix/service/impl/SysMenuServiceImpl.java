package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysMenuMapper;
import com.matrix.entity.po.SysMenu;
import com.matrix.service.SysMenuService;
import org.springframework.stereotype.Service;

/**
 * (SysMenu)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

}

