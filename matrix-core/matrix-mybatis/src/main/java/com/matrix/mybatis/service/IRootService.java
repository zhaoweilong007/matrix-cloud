package com.matrix.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.matrix.common.vo.PageParam;
import com.matrix.common.vo.PageResult;

import java.io.Serializable;

/**
 * The interface Root service.
 */
public interface IRootService<T, V> extends MPJBaseService<T> {

    /**
     * 幂等性新增记录
     *
     * @param entity       实体
     * @param countWrapper 计数包装
     * @return boolean
     * @throws Exception 异常
     */
    boolean saveIdempotency(T entity, Wrapper<T> countWrapper) throws Exception;

    /**
     * 幂等性新增或更新记录
     */
    boolean saveOrUpdateIdempotency(T entity, Wrapper<T> countWrapper) throws Exception;


    <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam);

    <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam, Wrapper<T> wrapper);

    T selectById(Serializable id);

    V selectVoById(Serializable id);

    /**
     * 根据条件判断记录是否存在
     */
    boolean exists(Wrapper<T> queryWrapper);

    <DTO> PageResult<DTO> selectJoinPage(PageParam query, Class<DTO> clazz, MPJBaseJoin<T> wrapper);
}
