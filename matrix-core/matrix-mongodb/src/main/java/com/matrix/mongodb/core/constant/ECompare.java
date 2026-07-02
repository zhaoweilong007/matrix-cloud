package com.matrix.mongodb.core.constant;

/**
 * 比较类型
 */
public enum ECompare {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NE,
    /**
     * 小于等于
     */
    LE,
    /**
     * 小于
     */
    LT,
    /**
     * 大于等于
     */
    GE,
    /**
     * 大于
     */
    GT,
    /**
     * 区间（左右均包括）
     */
    BW,
    /**
     * 包含
     */
    IN,
    /**
     * 不包含
     */
    NIN
}
