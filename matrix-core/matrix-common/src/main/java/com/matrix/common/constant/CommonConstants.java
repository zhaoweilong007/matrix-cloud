package com.matrix.common.constant;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/6
 **/
public interface CommonConstants {

    String USER_ID_HEADER = "user_id";
    String USER_MOBILE_HEADER = "user_mobile";
    String TENANT_ID_HEADER = "tenant_id";
    String VERSION_HEADER = "version";
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
    String LOGIN_USER_KEY = "loginUser";
    String USER_KEY = "userId";
    String TENANT_KEY = "tenantId";
    String USER_DEVICE = "User-Device";

    String USER_TYPE = "user_type";

    String PLATFORMS = "platforms";
}
