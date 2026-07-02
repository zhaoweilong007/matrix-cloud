package com.matrix.common.constant;

/**
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 * <p>
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 enum 包下
 */
public interface WebFilterOrderConstants {

    /**
     * CORS 过滤器顺序
     */
    int CORS_FILTER = Integer.MIN_VALUE;

    /**
     * Trace 过滤器顺序
     */
    int TRACE_FILTER = CORS_FILTER + 1;

    /**
     * 请求体缓存过滤器顺序
     */
    int REQUEST_BODY_CACHE_FILTER = Integer.MIN_VALUE + 500;

    /**
     * 验证码过滤器顺序
     */
    int VALIDATE_CODE_FILTER = REQUEST_BODY_CACHE_FILTER + 1;


    /**
     * 用户上下文过滤器顺序
     */
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

    /**
     * API 访问日志过滤器顺序
     */
    int API_ACCESS_LOG_FILTER = -100;


}
