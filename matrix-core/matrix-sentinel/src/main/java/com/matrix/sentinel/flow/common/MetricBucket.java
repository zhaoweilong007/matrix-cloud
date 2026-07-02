package com.matrix.sentinel.flow.common;

import java.util.concurrent.atomic.LongAdder;

/**
 * 一段时间内的度量数据
 *
 * from https://github.com/wujiuye/qps-helper
 */
public class MetricBucket {

    /**
     * 存储各事件的计数，比如异常总数、请求总数等
     */
    private final LongAdder[] counters;
    /**
     * 这段时间内的最小耗时
     */
    private volatile long minRt = Integer.MAX_VALUE;
    /**
     * 这段时间内的最大耗时
     */
    private volatile long maxRt = Integer.MIN_VALUE;

    public MetricBucket() {
        // 初始化数组
        MetricEvent[] events = MetricEvent.values();
        this.counters = new LongAdder[events.length];
        for (MetricEvent event : events) {
            counters[event.ordinal()] = new LongAdder();
        }
    }

    /**
     * 获取指定事件的计数
     *
     * @param event 事件类型
     * @return 计数
     */
    public long get(MetricEvent event) {
        return counters[event.ordinal()].sum();
    }

    private void add(MetricEvent event, long n) {
        counters[event.ordinal()].add(n);
    }

    /**
     * 重置所有计数
     */
    public void reset() {
        for (MetricEvent event : MetricEvent.values()) {
            counters[event.ordinal()].reset();
        }
    }

    /**
     * 获取异常总数
     */
    public long exception() {
        return get(MetricEvent.EXCEPTION);
    }

    /**
     * 获取最小耗时
     */
    public long minRt() {
        return minRt;
    }

    /**
     * 获取最大耗时
     */
    public long maxRt() {
        return maxRt;
    }

    /**
     * 获取总耗时
     */
    public long rt() {
        return get(MetricEvent.RT);
    }

    /**
     * 添加响应时间
     *
     * @param rt 响应时间（毫秒）
     */
    public void addRt(long rt) {
        add(MetricEvent.RT, rt);
        if (rt < minRt) {
            minRt = rt;
        }
        if (rt > maxRt) {
            maxRt = rt;
        }
    }

    /**
     * 获取成功总数
     */
    public long success() {
        return get(MetricEvent.SUCCESS);
    }

    /**
     * 添加异常数
     *
     * @param n 异常数
     */
    public void addException(int n) {
        add(MetricEvent.EXCEPTION, n);
    }

    /**
     * 添加成功数
     *
     * @param n 成功数
     */
    public void addSuccess(int n) {
        add(MetricEvent.SUCCESS, n);
    }
}
