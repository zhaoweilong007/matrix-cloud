package com.matrix.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.entity.vo.LoginUser;

/**
 * 用户上下文
 */
public class UserContextHolder {
    private static final ThreadLocal<LoginUser> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setContext(LoginUser loginUser) {
        THREAD_LOCAL.set(loginUser);
    }

    public static LoginUser getContext() {
        return THREAD_LOCAL.get();
    }

    public static String getUsername() {
        return THREAD_LOCAL.get().getUsername();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
