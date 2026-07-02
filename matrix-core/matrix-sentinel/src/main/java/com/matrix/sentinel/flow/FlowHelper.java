package com.matrix.sentinel.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * 流量统计助手
 * from https://github.com/wujiuye/qps-helper
 */
public class FlowHelper {

    /**
     * 流量统计类型标记位，通过位运算组合
     */
    private byte typeFlag;
    /**
     * 流量统计类型与统计器映射
     */
    private Map<FlowType, Flower> flowMap;

    /**
     * 构造流量统计助手，注册需要统计的时间维度
     *
     * @param types 统计的时间维度（秒/分/时）
     */
    public FlowHelper(FlowType... types) {
        flowMap = new HashMap<>();
        for (FlowType type : types) {
            typeFlag |= type.getFlag();
            flowMap.put(type, newFlow(type));
        }
    }

    private Flower newFlow(FlowType flowType) {
        switch (flowType) {
            case Minute:
                return new MinuteFlower();
            case Second:
                return new SecondFlower();
            case Hour:
                return new HourFlower();
            default:
                throw new RuntimeException("not supor type!");
        }
    }

    /**
     * 每接收一个请求将请求数自增1并添加耗时
     *
     * @param rt 该请求的耗时（毫秒为单位）
     */
    public void incrSuccess(long rt) {
        for (FlowType type : FlowType.values()) {
            if ((typeFlag & type.getFlag()) == type.getFlag()) {
                flowMap.get(type).incrSuccess(rt);
            }
        }
    }

    /**
     * 每出现一次异常，将异常总数自增1
     */
    public void incrException() {
        for (FlowType type : FlowType.values()) {
            if ((typeFlag & type.getFlag()) == type.getFlag()) {
                flowMap.get(type).incrException();
            }
        }
    }

    /**
     * 获取指定时间维度的流量统计器
     *
     * @param flowType 流量统计类型
     * @return 流量统计器
     */
    public Flower getFlow(FlowType flowType) {
        return flowMap.get(flowType);
    }

    /**
     * 获取所有时间维度的流量统计器映射
     *
     * @return 流量统计器映射
     */
    public Map<FlowType, Flower> getFlowMap() {
        return new HashMap<>(flowMap);
    }
}
