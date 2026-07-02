package com.matrix.sentinel.flow;

/**
 * 流量统计时间维度类型
 *
 * from https://github.com/wujiuye/qps-helper
 */
public enum FlowType {

    /**
     * 秒
     */
    Second((byte) 0b00000001),
    /**
     * 分
     */
    Minute((byte) 0b00000010),
    /**
     * 小时
     */
    Hour((byte) 0b00000100);

    /**
     * 类型标记位
     */
    byte flag;

    FlowType(byte flag) {
        this.flag = flag;
    }

    /**
     * 获取类型标记位
     *
     * @return 标记位
     */
    public byte getFlag() {
        return flag;
    }
}
