package com.matrix.mybatis.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.util.SqlUtil;
import com.matrix.common.vo.PageParam;
import com.matrix.common.vo.PageResult;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 描述：<p>分页查询工具类</p>
 *
 * @author ZhaoWeiLong
 * @since 2023/3/8
 **/
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNum() - 1) * pageParam.getPageSize();
    }


    /**
     * 执行分页请求
     *
     * @param query 查询参数
     * @param fun   执行方法
     * @param <Q>   查询参数
     * @param <E>   返回实体
     * @return ResultPageVo
     */
    public static <Q extends PageParam, E> PageResult<E> pageQuery(Q query, BiFunction<Page<E>, Q, Page<E>> fun) {
        final Page<E> result = fun.apply(build(query), query);
        return PageResult.<E>builder()
                .pageNum((int) result.getCurrent())
                .pageSize((int) result.getSize())
                .total((int) result.getTotal())
                .data(result.getRecords()).build();
    }


    public static <Q extends PageParam, E, T> PageResult<E> pageQuery(Q query, Wrapper<T> wrapper, BiFunction<Page<T>, Wrapper<T>, Page<E>> fun) {
        final Page<E> result = fun.apply(build(query), wrapper);
        return PageResult.<E>builder()
                .pageNum((int) result.getCurrent())
                .pageSize((int) result.getSize())
                .total((int) result.getTotal())
                .data(result.getRecords()).build();
    }


    /**
     * mybatis plus使用
     *
     * @param query
     * @param <T>
     * @param <Q>
     * @return
     */
    public static <T, Q extends PageParam> Page<T> build(Q query) {
        Integer pageNum = ObjectUtil.defaultIfNull(query.getPageNum(), PageParam.PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(query.getPageSize(), PageParam.PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = PageParam.PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItems = buildOrderItem(query);
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 构建排序
     * <p>
     * 支持的用法如下: {isAsc:"asc",orderByColumn:"id"} order by id asc {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id
     * asc,create_time desc
     */
    private static <Q extends PageParam> List<OrderItem> buildOrderItem(Q query) {
        String orderByColumn = query.getSort();
        String isAsc = query.getOrder();
        if (StringUtils.isBlank(orderByColumn) || StringUtils.isBlank(isAsc)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        orderBy = StrUtil.toUnderlineCase(orderBy);

        // 兼容前端排序类型
        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(",");
        String[] isAscArr = isAsc.split(",");
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR);
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equalsIgnoreCase(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equalsIgnoreCase(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR);
            }
        }
        return list;
    }


    public static <Q> PageResult<Q> buildPageResult(Page<Q> page) {
        return PageResult.<Q>builder()
                .pageNum((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .total((int) page.getTotal())
                .data(page.getRecords()).build();
    }

    public static <E> PageResult<E> buildPageResultEmpty(PageResult pageResult, List<E> data) {
        return PageResult.<E>builder()
                .pageNum(pageResult.getPageNum())
                .pageSize(pageResult.getPageSize())
                .total(pageResult.getTotal())
                .data(data).build();
    }

    public static <Q> PageResult<Q> buildPageResult(Page page, List<Q> records) {
        return PageResult.<Q>builder()
                .pageNum((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .total((int) page.getTotal())
                .data(records).build();
    }

    public static <Q> PageResult<Q> buildPageResult(PageResult page, List<Q> records) {
        return PageResult.<Q>builder()
                .pageNum((int) page.getPageNum())
                .pageSize((int) page.getPageSize())
                .total((int) page.getTotal())
                .data(records).build();
    }
}
