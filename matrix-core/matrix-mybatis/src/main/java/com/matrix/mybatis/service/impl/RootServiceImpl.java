package com.matrix.mybatis.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.matrix.common.vo.PageParam;
import com.matrix.common.vo.PageResult;
import com.matrix.mybatis.mapper.BaseMapperX;
import com.matrix.mybatis.service.IRootService;

import java.io.Serializable;
import java.util.Objects;

/**
 * IRootService实现类
 */
public class RootServiceImpl<M extends BaseMapperX<M, T, V>, T, V> extends MPJBaseServiceImpl<M, T> implements IRootService<T, V> {


    @Lock4j(name = "rootService", keys = "#lockKey-#entity.getClass")
    @Override
    public boolean saveIdempotency(T entity, Wrapper<T> countWrapper) {
        long count = super.count(countWrapper);
        Assert.isTrue(count == 0, "记录已存在");
        return super.save(entity);
    }


    @Override
    @Lock4j(name = "rootService", keys = "#lockKey-#entity.getClass")
    public boolean saveOrUpdateIdempotency(T entity, Wrapper<T> countWrapper) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    return this.save(entity);
                } else {
                    return updateById(entity);
                }
            } else {
                throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }


    @Override
    public <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam) {
        return this.getBaseMapper().selectVoPage(pageParam);
    }

    @Override
    public <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam, Wrapper<T> wrapper) {
        return this.getBaseMapper().selectVoPage(pageParam, wrapper);
    }

    @Override
    public V selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);
    }

    @Override
    public boolean exists(Wrapper<T> queryWrapper) {
        return this.getBaseMapper().exists(queryWrapper);
    }

    @Override
    public T selectById(Serializable id) {
        return this.getBaseMapper().selectById(id);
    }

    @Override
    public <DTO> PageResult<DTO> selectJoinPage(PageParam query, Class<DTO> clazz, MPJBaseJoin<T> wrapper) {
        return baseMapper.selectJoinPage(query, clazz, wrapper);
    }
}
