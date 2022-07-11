package com.matrix.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.mapper.SysResourceCategoryMapper;
import com.matrix.entity.po.SysResourceCategory;
import com.matrix.service.SysResourceCategoryService;
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

