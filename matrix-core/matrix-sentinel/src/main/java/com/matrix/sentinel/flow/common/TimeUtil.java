package com.matrix.sentinel.flow.common;

import java.util.concurrent.TimeUnit;

/**
 * 时间计算工具
 *
 * from https://github.com/wujiuye/qps-helper
 */
public final class TimeUtil {

    /**
     * 当前时间戳缓存（毫秒），由守护线程每秒更新
     */
    private static volatile long currentTimeMillis;

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    currentTimeMillis = System.currentTimeMillis();
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Throwable e) {

                    }
                }
            }
        });
        daemon.setDaemon(true);
        daemon.setName("qps-helper-time-tick-thread");
        daemon.start();
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}
