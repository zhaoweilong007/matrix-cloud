package com.matrix.mongodb.core.wrapper;

/**
 * Wrapper 条件构造
 */
public final class Wrappers {

    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

}
