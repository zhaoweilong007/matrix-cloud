package com.matrix.gateway.order;

import org.springframework.core.Ordered;

/**
 * 定义过滤器排序
 *
 * @author ZhaoWeiLong
 * @since 2023/6/26
 **/
public interface FilterOrder {

    int GLOBAL_CORS_FILTER = Ordered.HIGHEST_PRECEDENCE;
    int GLOBAL_CACHE_REQUEST_FILTER = Ordered.HIGHEST_PRECEDENCE + 2;
    int GLOBAL_LOG_FILTER = Ordered.HIGHEST_PRECEDENCE + 3;
    int GLOBAL_I18N_FILTER = Ordered.HIGHEST_PRECEDENCE + 4;
    int XSS_FILTER = Ordered.HIGHEST_PRECEDENCE + 5;
    int BLACK_LIST_URL_FILTER = Ordered.HIGHEST_PRECEDENCE + 6;
    int FORWARD_AUTH_FILTER = -100;
    int DEFLECTION_INTANCE_FILTER = 10150;
    int GRAY_VERSION_ISOLATION_FILTER = 10150;
    int IP_HASH_LOAD_BALANCER_CLIENT_FILTER = 10150;


}
