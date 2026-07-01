package com.matrix.mybatis.query;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import java.util.Collection;
import java.util.function.Consumer;
import org.springframework.util.StringUtils;

/**
 * 拓展 MyBatis-Plus-Join MPJLambdaWrapper，增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，判断值不存在时不拼接条件。
 * 2. Join 方法增加 Consumer 回调扩展，支持链式追加额外条件。
 *
 * @param <T> 主表数据类型
 * @author matrix
 */
public class MPJLambdaWrapperX<T> extends MPJLambdaWrapper<T> {

    // ========== xxxIfPresent 方法族 ==========

    public MPJLambdaWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (MPJLambdaWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (MPJLambdaWrapperX<T>) super.likeRight(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (MPJLambdaWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (!ArrayUtil.isEmpty(values)) {
            return (MPJLambdaWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (MPJLambdaWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (MPJLambdaWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (MPJLambdaWrapperX<T>) le(column, val2);
        }
        return this;
    }

    // ========== Join 方法增强（Consumer 回调扩展） ==========

    /**
     * 左连接，带 Consumer 回调扩展。
     *
     * @param clazz 关联表实体类
     * @param left  左表关联字段
     * @param right 右表关联字段
     * @param ext   额外条件回调（可为 null）
     * @param <A>   左表类型
     * @param <B>   右表类型
     * @return this
     */
    public <A, B> MPJLambdaWrapperX<T> leftJoin(Class<A> clazz, SFunction<A, ?> left,
            SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
        super.leftJoin(clazz, left, right);
        if (ext != null) {
            ext.accept(this);
        }
        return this;
    }

    /**
     * 右连接，带 Consumer 回调扩展。
     */
    public <A, B> MPJLambdaWrapperX<T> rightJoin(Class<A> clazz, SFunction<A, ?> left,
            SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
        super.rightJoin(clazz, left, right);
        if (ext != null) {
            ext.accept(this);
        }
        return this;
    }

    /**
     * 内连接，带 Consumer 回调扩展。
     */
    public <A, B> MPJLambdaWrapperX<T> innerJoin(Class<A> clazz, SFunction<A, ?> left,
            SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
        super.innerJoin(clazz, left, right);
        if (ext != null) {
            ext.accept(this);
        }
        return this;
    }

    // ========== 重写父类方法，保证返回 MPJLambdaWrapperX 类型 ==========

    @Override
    public <A, B> MPJLambdaWrapperX<T> leftJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
        super.leftJoin(clazz, left, right);
        return this;
    }

    @Override
    public <A, B> MPJLambdaWrapperX<T> rightJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
        super.rightJoin(clazz, left, right);
        return this;
    }

    @Override
    public <A, B> MPJLambdaWrapperX<T> innerJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
        super.innerJoin(clazz, left, right);
        return this;
    }
}
