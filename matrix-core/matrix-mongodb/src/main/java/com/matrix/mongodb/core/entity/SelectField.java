package com.matrix.mongodb.core.entity;

/**
 * 查询的列
 */
public class SelectField {

    /**
     * 集合对应的列
     */
    private String col;

    public SelectField() {
    }

    public SelectField(String col) {
        this.col = col;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }
}
