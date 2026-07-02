package com.matrix.common.constant;

/**
 * 通用常量定义
 */
public interface CommonConstants {

    /**
     * 用户 ID 请求头
     */
    String USER_ID_HEADER = "user_id";
    /**
     * 用户手机号请求头
     */
    String USER_MOBILE_HEADER = "user_mobile";
    /**
     * 租户 ID 请求头
     */
    String TENANT_ID_HEADER = "tenant_id";
    /**
     * 版本号请求头
     */
    String VERSION_HEADER = "version";
    /**
     * Token 请求头
     */
    String TOKEN_HEADER = "Authorization";
    /**
     * 负载均衡策略-权重
     */
    String WEIGHT_KEY = "weight";
    /**
     * 负载均衡策略-偏向性 ip:port
     */
    String INSTANCE_INFO = "instance";
    /**
     * 灰度网关
     */
    String GRAY_LB = "grayLb";
    /**
     * hash网关
     */
    String HASH_LB = "iphash";
    /**
     * 登录用户上下文 Key
     */
    String LOGIN_USER_KEY = "loginUser";
    /**
     * 用户上下文 Key
     */
    String USER_KEY = "userId";
    /**
     * 租户上下文 Key
     */
    String TENANT_KEY = "tenantId";
    /**
     * 用户设备上下文 Key
     */
    String USER_DEVICE = "User-Device";

    /**
     * 用户类型上下文 Key
     */
    String USER_TYPE = "user_type";

    /**
     * 平台标识上下文 Key
     */
    String PLATFORMS = "platforms";
}
