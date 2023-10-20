package com.matrix.mybatis.base;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.matrix.api.api.IBaseFeignClient;
import com.matrix.common.entity.BaseIdEntity;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import com.matrix.common.util.IndistUtils;
import com.matrix.common.util.StringUtils;
import com.matrix.common.vo.PageMultParam;
import com.matrix.common.vo.PageParam;
import com.matrix.common.vo.PageResult;
import com.matrix.mybatis.service.IRootService;
import com.matrix.validator.group.AddGroup;
import com.matrix.validator.group.EditGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 基础通用controller
 *
 * @author ZhaoWeiLong
 * @since 2023/4/13
 **/
@Slf4j
@Validated
public abstract class BaseController<S extends IRootService<T, V>, T extends BaseIdEntity, V, Q extends PageParam> implements IBaseFeignClient<T, V, Q> {


    @Autowired
    public S service;

    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link R}<{@link T}>
     */
    @Override
    public R<T> queryById(@RequestParam Long id) {
        return R.success(service.getById(id));
    }


    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link R}<{@link T}>
     */
    @Override
    public R<V> queryVoById(@RequestParam Long id) {
        return R.success(service.selectVoById(id));
    }


    /**
     * 保存
     *
     * @param entity 实体
     * @return {@link R}<{@link Long}>
     */
    @Override
    public R<Long> save(@RequestBody @Validated(value = AddGroup.class) T entity) {
        final boolean save = service.save(entity);
        return save ? R.success(entity.getId()) : R.fail(SystemErrorTypeEnum.OPERATE_FAIL);
    }


    /**
     * 更新通过id
     *
     * @param entity 实体
     * @return {@link R}<{@link Boolean}>
     */
    @Override
    public R<Boolean> updateById(@RequestBody @Validated(value = EditGroup.class) T entity) {
        final T t = service.getById(entity.getId());
        Assert.notNull(t, () -> new ServiceException(BusinessErrorTypeEnum.RECORD_NOT_EXIST));
        final boolean update = service.updateById(entity);
        return update ? R.success(true) : R.fail(SystemErrorTypeEnum.OPERATE_FAIL);
    }


    /**
     * 分页查询
     *
     * @param query 查询
     * @return {@link R}<{@link PageResult}<{@link V}>>
     */
    @Override
    public R<PageResult<V>> pageQuery(@RequestBody Q query) {
        final PageResult<V> page = service.selectVoPage(query, buildPageQueryWrapper(query));
        return R.success(page);
    }

    public Wrapper<T> buildPageQueryWrapper(Q query) {
        return new QueryWrapper<>();
    }


    /**
     * 删除通过id
     *
     * @param id id
     * @return {@link R}<{@link Boolean}>
     */
    @Override
    public R<Boolean> deleteById(@RequestParam Long id) {
        final boolean remove = service.removeById(id);
        return remove ? R.success() : R.fail(SystemErrorTypeEnum.OPERATE_FAIL);
    }

    /**
     * 分页参数转换,单独抽出来处理
     *
     * @param param
     * @return
     */
    public PageMultParam<Map<String, Object>> proPageParam(Map<String, Object> param) {

        PageMultParam<Map<String, Object>> page = new PageMultParam<>();

        //关闭分页忧化,否则查询总数与查询列表数据时sql不一致
        page.setOptimizeCountSql(false);

        //设置默认分布参数
        page.setPageNum(1);
        page.setPageSize(10);

        if (IndistUtils.isNotEmpty(param.get("pageNum"))) {
            page.setPageNum((Integer) param.get("pageNum"));
        }

        if (IndistUtils.isNotEmpty(param.get("pageSize"))) {
            Integer pageSize = Integer.parseInt(String.valueOf(param.get("pageSize")));
            page.setPageSize(pageSize > 100 ? 100 : pageSize);
        }

        param.remove("pageNum");
        param.remove("pageSize");

        //配置排序规则
        if (null != param.get("sort")) {
            String sortField = String.valueOf(param.get("sort"));
            if (StringUtils.isNotEmpty(sortField)) {
                param.put("sort", StrUtil.toUnderlineCase(sortField));

                if (null == param.get("order")) {
                    param.put("order", "desc");
                }
            }
        }

        page.setData(param);

        return page;
    }
}
