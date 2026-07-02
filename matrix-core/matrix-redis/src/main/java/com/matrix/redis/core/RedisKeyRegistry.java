package com.matrix.redis.core;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RedisKeyDefine} 注册表
 */
public class RedisKeyRegistry {

    /**
     * Redis RedisKeyDefine 数组
     */
    private static final List<RedisKeyDefine> DEFINES = new ArrayList<>();

    /**
     * 注册 Key 定义。
     *
     * @param define Redis Key 定义
     */
    public static void add(RedisKeyDefine define) {
        DEFINES.add(define);
    }

    /**
     * 获取所有已注册的 Key 定义列表。
     *
     * @return Key 定义列表
     */
    public static List<RedisKeyDefine> list() {
        return DEFINES;
    }

    /**
     * 获取已注册的 Key 定义数量。
     *
     * @return 数量
     */
    public static int size() {
        return DEFINES.size();
    }
}
