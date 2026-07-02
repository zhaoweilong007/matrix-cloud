package com.matrix.mongodb.core.wrapper;

/**
 * Wrapper 条件构造
 */
public final class Wrappers {

    /**
     * 创建 Lambda 查询条件构造器
     *
     * @param <T> 实体类型
     * @return Lambda 查询条件构造器
     */
    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }
}
