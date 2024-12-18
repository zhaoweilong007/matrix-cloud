package com.matrix.mongodb.core.entity;


import com.matrix.mongodb.core.constant.ECompare;
import com.matrix.mongodb.core.constant.EConditionType;
import com.matrix.mongodb.core.wrapper.ConditionWrapper;

import java.util.List;

/**
 * 条件
 */
public class Condition {

    /**
     * 嵌套条件比较类型
     */
    private EConditionType conditionType = EConditionType.AND;
    /**
     * 比较类型
     */
    private ECompare type;
    /**
     * 集合对应的列
     */
    private String col;
    /**
     * 比较值
     */
    private List<Object> args;
    /**
     * 条件包装类
     */
    private ConditionWrapper conditionWrapper;

    public Condition() {
    }

    public Condition(ECompare type, String col, List<Object> args) {
        this.type = type;
        this.col = col;
        this.args = args;
    }

    public EConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(EConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public ECompare getType() {
        return type;
    }

    public void setType(ECompare type) {
        this.type = type;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public ConditionWrapper getConditionWrapper() {
        return conditionWrapper;
    }

    public void setConditionWrapper(ConditionWrapper conditionWrapper) {
        this.conditionWrapper = conditionWrapper;
    }
}
