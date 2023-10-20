package com.matrix.common.constant;

/**
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 * <p>
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 enum 包下
 */
public interface WebFilterOrderConstants {

    int CORS_FILTER = Integer.MIN_VALUE;

    int TRACE_FILTER = CORS_FILTER + 1;

    int REQUEST_BODY_CACHE_FILTER = Integer.MIN_VALUE + 500;

    int VALIDATE_CODE_FILTER = REQUEST_BODY_CACHE_FILTER + 1;


    int USER_CONTEXT_FILTER = -99;

    /**
     * 上下文
     */
    int TENANT_CONTEXT_FILTER = -98;

    /**
     * 用户类型上下文
     */
    int USER_DEVICE_FILETER = -97;

    /**
     * 租户校验
     */
    int TENANT_SECURITY_FILTER = -96;


}
