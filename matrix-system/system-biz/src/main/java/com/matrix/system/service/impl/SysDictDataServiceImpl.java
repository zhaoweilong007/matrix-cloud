package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysDictData;
import com.matrix.system.mapper.SysDictDataMapper;
import com.matrix.system.service.SysDictDataService;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * SysDictData 服务实现。
 *
 * <p>字典数据按类型缓存，写操作自动驱逐缓存。</p>
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Override
    @Cacheable(value = "dictDataCache", key = "#dictType")
    public List<SysDictData> listByType(String dictType) {
        return this.list(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 1)
                .orderByAsc(SysDictData::getSort));
    }

    @Override
    @CacheEvict(value = "dictDataCache", allEntries = true)
    public boolean save(SysDictData entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(value = "dictDataCache", allEntries = true)
    public boolean updateById(SysDictData entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(value = "dictDataCache", allEntries = true)
    public boolean removeById(java.io.Serializable id) {
        return super.removeById(id);
    }
}
