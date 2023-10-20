package com.matrix.mybatis.mapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.matrix.common.vo.PageParam;
import com.matrix.common.vo.PageResult;
import com.matrix.mybatis.utils.PageUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 在 MyBatis Plus 的 BaseMapper 的基础上拓展，提供更多的能力
 *
 * @param <M> mapper 泛型
 * @param <T> table 泛型
 * @param <V> vo 泛型
 */
@SuppressWarnings("unchecked")
public interface BaseMapperX<M, T, V> extends MPJBaseMapper<T> {

    Log log = LogFactory.getLog(BaseMapperX.class);

    /**
     * 根据 entity 条件，查询全部记录，并锁定
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<T> selectListForUpdate(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);


    /**
     * 选择一个用于更新
     *
     * @param queryWrapper 查询包装
     * @return {@link T}
     */
    T selectOneForUpdate(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);


    default Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperX.class, 1);
    }

    default Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperX.class, 0);
    }

    default Class<V> currentVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperX.class, 2);
    }


    default <Q extends PageParam> PageResult<T> selectPage(Q pageParam, Wrapper<T> queryWrapper) {
        // MyBatis Plus 查询
        IPage<T> mpPage = PageUtils.build(pageParam);
        selectPage(mpPage, queryWrapper);
        // 转换返回
        return PageResult.<T>builder().pageNum((int) mpPage.getCurrent()).pageSize((int) mpPage.getSize()).total((int) mpPage.getTotal()).data(mpPage.getRecords())
                .build();
    }


    /**
     * 批量插入
     */
    default boolean insertBatch(Collection<T> entityList) {
        return Db.saveBatch(entityList);
    }

    /**
     * 批量更新
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return Db.updateBatchById(entityList);
    }

    /**
     * 批量插入或更新
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList) {
        return Db.saveOrUpdateBatch(entityList);
    }

    /**
     * 批量插入(包含限制条数)
     */
    default boolean insertBatch(Collection<T> entityList, int batchSize) {
        return Db.saveBatch(entityList, batchSize);
    }

    /**
     * 批量更新(包含限制条数)
     */
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return Db.updateBatchById(entityList, batchSize);
    }

    /**
     * 批量插入或更新(包含限制条数)
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return Db.saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * 插入或更新(包含限制条数)
     */
    default boolean insertOrUpdate(T entity) {
        return Db.saveOrUpdate(entity);
    }

    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default Long selectCount() {
        return selectCount(new QueryWrapper<T>());
    }

    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(String field, Collection<?> values) {
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> leField, SFunction<T, ?> geField, Object value) {
        return selectList(new LambdaQueryWrapper<T>().le(leField, value).ge(geField, value));
    }


    default void updateBatch(T update) {
        update(update, new QueryWrapper<>());
    }


    default V selectVoById(Serializable id) {
        return selectVoById(id, this.currentVoClass());
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanUtil.toBeanIgnoreError(obj, voClass);
    }

    default List<V> selectVoBatchIds(Collection<? extends Serializable> idList) {
        return selectVoBatchIds(idList, this.currentVoClass());
    }

    /**
     * 查询（根据ID 批量查询）
     */
    default <C> List<C> selectVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        if (CollUtil.isEmpty(idList)) {
            return CollUtil.newArrayList();
        }
        List<T> list = this.selectBatchIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanUtil.copyToList(list, voClass);
    }

    default List<V> selectVoByMap(Map<String, Object> map) {
        return selectVoByMap(map, this.currentVoClass());
    }

    /**
     * 查询（根据 columnMap 条件）
     */
    default <C> List<C> selectVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = this.selectByMap(map);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanUtil.copyToList(list, voClass);
    }

    default V selectVoOne(Wrapper<T> wrapper) {
        return selectVoOne(wrapper, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    default <C> C selectVoOne(Wrapper<T> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanUtil.toBeanIgnoreError(obj, voClass);
    }

    default V selectVoOne(SFunction<T, ?> field, Object value) {
        return selectVoOne(new LambdaQueryWrapper<T>().eq(field, value), this.currentVoClass());
    }

    default List<V> selectVoList(Wrapper<T> wrapper) {
        return selectVoList(wrapper, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询全部记录
     */
    default <C> List<C> selectVoList(Wrapper<T> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanUtil.copyToList(list, voClass);
    }

    default <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam) {
        return selectVoPage(pageParam, new QueryWrapper<>(), this.currentVoClass());
    }

    default <Q extends PageParam> PageResult<V> selectVoPage(Q pageParam, Wrapper<T> wrapper) {
        return selectVoPage(pageParam, wrapper, this.currentVoClass());
    }

    default <Q extends PageParam, E> PageResult<E> selectVoPage(Q pageParam, Wrapper<T> wrapper, Class<E> voClass) {
        final Page<T> page = PageUtils.build(pageParam);
        final IPage<E> voPage = selectVoPage(page, wrapper, voClass);
        // 转换返回
        return PageResult.<E>builder().pageNum((int) voPage.getCurrent()).pageSize((int) voPage.getSize()).total((int) voPage.getTotal()).data(voPage.getRecords())
                .build();
    }

    default <E> IPage<E> selectVoPage(IPage<T> page, Wrapper<T> wrapper) {
        final Class<E> vClass = (Class<E>) this.currentVoClass();
        return selectVoPage(page, wrapper, vClass);
    }


    /**
     * 分页查询VO
     */
    default <E> IPage<E> selectVoPage(IPage<T> page, @Param("ew") Wrapper<T> wrapper, Class<E> vClass) {
        IPage<T> pageData = this.selectPage(page, wrapper);
        IPage<E> voPage = new Page<>(pageData.getCurrent(), pageData.getSize(), pageData.getTotal());
        if (CollUtil.isEmpty(pageData.getRecords())) {
            return voPage;
        }
        voPage.setRecords(BeanUtil.copyToList(pageData.getRecords(), vClass));
        return voPage;
    }

    default <DTO> PageResult<DTO> selectJoinPage(PageParam query, Class<DTO> clazz, MPJBaseJoin<T> wrapper) {
        final Page<DTO> page = PageUtils.build(query);
        final Page<DTO> pageData = selectJoinPage(page, clazz, wrapper);
        return PageResult.<DTO>builder().pageNum((int) pageData.getCurrent())
                .pageSize((int) pageData.getSize()).total((int) pageData.getTotal())
                .data(pageData.getRecords())
                .build();
    }
}
