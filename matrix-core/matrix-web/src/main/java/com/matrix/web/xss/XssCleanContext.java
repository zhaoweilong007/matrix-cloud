package com.matrix.web.xss;

/** XSS 请求清理上下文。 */
final class XssCleanContext {
    private static final ThreadLocal<Boolean> SKIPPED = new ThreadLocal<>();

    private XssCleanContext() {
    }

    static boolean isSkipped() {
        return Boolean.TRUE.equals(SKIPPED.get());
    }

    static void setSkipped(boolean skipped) {
        if (skipped) {
            SKIPPED.set(Boolean.TRUE);
        }
    }

    static void clear() {
        SKIPPED.remove();
    }
}
