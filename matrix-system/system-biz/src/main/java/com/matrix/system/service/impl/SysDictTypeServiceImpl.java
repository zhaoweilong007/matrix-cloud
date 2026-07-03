package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysDictType;
import com.matrix.system.mapper.SysDictTypeMapper;
import com.matrix.system.service.SysDictTypeService;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * SysDictType 服务实现。
 *
 * <p>字典类型列表缓存，写操作自动驱逐缓存。</p>
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Override
    @Cacheable(value = "dictTypeCache")
    public List<SysDictType> list() {
        return super.list();
    }

    @Override
    @CacheEvict(value = "dictTypeCache", allEntries = true)
    public boolean save(SysDictType entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(value = "dictTypeCache", allEntries = true)
    public boolean updateById(SysDictType entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(value = "dictTypeCache", allEntries = true)
    public boolean removeById(java.io.Serializable id) {
        return super.removeById(id);
    }
}
