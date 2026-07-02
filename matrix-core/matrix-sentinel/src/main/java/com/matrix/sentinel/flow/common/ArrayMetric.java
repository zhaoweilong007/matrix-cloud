package com.matrix.sentinel.flow.common;

import java.util.List;

/**
 * 数组度量器
 *
 * from https://github.com/wujiuye/qps-helper
 */
public class ArrayMetric implements Metric {

    /**
     * 滑动窗口数据
     */
    private final LeapArray<MetricBucket> data;

    /**
     * 构造数组度量器
     *
     * @param sampleCount  样本数
     * @param intervalInMs 统计区间（毫秒）
     */
    public ArrayMetric(int sampleCount, int intervalInMs) {
        this.data = new BucketLeapArray(sampleCount, intervalInMs);
    }

    /**
     * 获取成功总数
     */
    @Override
    public long success() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long success = 0;
        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            success += window.success();
        }
        return success;
    }

    /**
     * 获取异常总数
     */
    @Override
    public long exception() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long exception = 0;
        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            exception += window.exception();
        }
        return exception;
    }

    /**
     * 获取总响应时间
     */
    @Override
    public long rt() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long rt = 0;
        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            rt += window.rt();
        }
        return rt;
    }

    /**
     * 获取最小响应时间
     */
    @Override
    public long minRt() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long rt = 0;
        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            if (window.minRt() < rt || rt == 0) {
                rt = window.minRt();
            }
        }
        return Math.max(1, rt);
    }

    /**
     * 获取最大响应时间
     */
    @Override
    public long maxRt() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long rt = 0;
        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            if (window.maxRt() > rt) {
                rt = window.maxRt();
            }
        }
        return Math.max(1, rt);
    }

    /**
     * 获取所有 bucket
     */
    @Override
    public MetricBucket[] buckets() {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        return data.values().toArray(new MetricBucket[0]);
    }

    /**
     * 获取所有滑动窗口
     */
    @Override
    public List<WindowWrap<MetricBucket>> windows() {
        return data.list();
    }

    /**
     * 添加异常数
     */
    @Override
    public void addException(int count) {
        WindowWrap<MetricBucket> wrap = data.currentWindow();
        wrap.value().addException(count);
    }

    /**
     * 添加成功数
     */
    @Override
    public void addSuccess(int count) {
        WindowWrap<MetricBucket> wrap = data.currentWindow();
        wrap.value().addSuccess(count);
    }

    /**
     * 添加响应时间
     */
    @Override
    public void addRt(long rt) {
        WindowWrap<MetricBucket> wrap = data.currentWindow();
        wrap.value().addRt(rt);
    }

    /**
     * 获取给定事件的总数
     *
     * @param event 要计算的事件
     * @return 总的计数
     */
    public long getSum(MetricEvent event) {
        // 确保当前时间的bucket不为空
        data.currentWindow();
        long sum = 0;
        List<MetricBucket> buckets = data.values();
        for (MetricBucket bucket : buckets) {
            sum += bucket.get(event);
        }
        return sum;
    }

    /**
     * 获取窗口长度（毫秒）
     */
    @Override
    public long getWindowInterval() {
        return data.getIntervalInMs();
    }

    /**
     * 获取样本数
     */
    @Override
    public int getSampleCount() {
        return data.getSampleCount();
    }
}
