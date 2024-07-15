package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡策略Holder
 */
public class LbIsolationContextHolder {
    private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

    public static String getVersion() {
        return VERSION_CONTEXT.get();
    }

    public static void setVersion(String version) {
        VERSION_CONTEXT.set(version);
    }

    public static void clear() {
        VERSION_CONTEXT.remove();
    }
}
