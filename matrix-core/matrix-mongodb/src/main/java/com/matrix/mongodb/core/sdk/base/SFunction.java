package com.matrix.mongodb.core.sdk.base;

import java.io.Serializable;

/**
 * 支持序列化的 Function
 * 为了获取字段名字
 */
@FunctionalInterface
public interface SFunction<T, R> extends Serializable {

    R apply(T t);

}
