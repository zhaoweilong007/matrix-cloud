package com.matrix.mongodb.core.entity;


import com.matrix.mongodb.core.constant.ESortType;

/**
 * 排序字段
 */
public class SortCondition {

    /**
     * 排序类型
     */
    private ESortType sortType = ESortType.ASC;

    /**
     * 集合对应的列
     */
    private String col;

    public SortCondition() {
    }

    public SortCondition(ESortType sortType, String col) {
        this.sortType = sortType;
        this.col = col;
    }

    public ESortType getSortType() {
        return sortType;
    }

    public void setSortType(ESortType sortType) {
        this.sortType = sortType;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }
}
