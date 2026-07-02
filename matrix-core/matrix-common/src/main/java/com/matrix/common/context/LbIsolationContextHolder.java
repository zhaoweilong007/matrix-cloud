package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡策略Holder
 */
public class LbIsolationContextHolder {
    /**
     * 灰度版本号上下文
     */
    private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

    public static String getVersion() {
        return VERSION_CONTEXT.get();
    }

    public static void setVersion(String version) {
        VERSION_CONTEXT.set(version);
    }

    /**
     * 清除上下文
     */
    public static void clear() {
        VERSION_CONTEXT.remove();
    }
}
