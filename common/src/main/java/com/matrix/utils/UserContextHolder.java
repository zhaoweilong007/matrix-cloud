package com.matrix.utils;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * 用户上下文
 */
public class UserContextHolder {

    private final ThreadLocal<Map<String, String>> threadLocal;

    private UserContextHolder() {
        this.threadLocal = new ThreadLocal<>();
    }

    public static final UserContextHolder INSTANCE = new UserContextHolder();

    public void setContext(Map<String, String> map) {
        threadLocal.set(map);
    }

    public Map<String, String> getContext() {
        return threadLocal.get();
    }

    public String getUsername() {
        return Optional.ofNullable(threadLocal.get()).orElse(Maps.newHashMap()).get("user_name");
    }

    public void clear() {
        threadLocal.remove();
    }

}
