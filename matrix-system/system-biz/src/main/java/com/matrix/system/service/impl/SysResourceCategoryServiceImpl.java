package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysResourceCategory;
import com.matrix.system.mapper.SysResourceCategoryMapper;
import com.matrix.system.service.SysResourceCategoryService;
import org.springframework.stereotype.Service;

/**
 * (SysResourceCategory)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Service("sysResourceCategoryService")
public class SysResourceCategoryServiceImpl extends ServiceImpl<SysResourceCategoryMapper, SysResourceCategory> implements SysResourceCategoryService {

}

