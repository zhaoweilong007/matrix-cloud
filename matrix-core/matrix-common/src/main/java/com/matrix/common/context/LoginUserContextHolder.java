package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.common.model.login.LoginUser;

/**
 * 登录用户holder
 */
public class LoginUserContextHolder {
    /**
     * 登录用户上下文
     */
    private static final ThreadLocal<LoginUser> CONTEXT = new TransmittableThreadLocal<LoginUser>();

    public static LoginUser getUser() {
        return CONTEXT.get();
    }

    public static void setUser(LoginUser user) {
        CONTEXT.set(user);
    }

    /**
     * 清除上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }
}