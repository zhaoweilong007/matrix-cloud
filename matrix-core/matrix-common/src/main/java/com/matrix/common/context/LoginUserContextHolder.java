package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.common.model.login.LoginUser;

/**
 * 登录用户holder
 */
public class LoginUserContextHolder {
    private static final ThreadLocal<LoginUser> CONTEXT = new TransmittableThreadLocal<LoginUser>();

    public static LoginUser getUser() {
        return CONTEXT.get();
    }

    public static void setUser(LoginUser user) {
        CONTEXT.set(user);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}