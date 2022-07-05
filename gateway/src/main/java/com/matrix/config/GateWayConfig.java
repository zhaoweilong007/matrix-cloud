package com.matrix.config;

import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/4 15:26
 **/
@Configuration
public class GateWayConfig {

    public static final long DEFAULT_TIMEOUT = 30000;

    public static String NACOS_SERVER_ADDR = "localhost:8848";

    public static String NACOS_ROUTE_DATA_ID = "gateway-route";

    public static String NACOS_ROUTE_GROUP = "DEFAULT_GROUP";


}
