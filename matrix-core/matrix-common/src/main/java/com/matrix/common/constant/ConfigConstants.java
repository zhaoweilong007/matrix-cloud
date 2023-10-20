package com.matrix.common.constant;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/12
 **/
public interface ConfigConstants {

    /**
     * 异常通知
     */
    String EXCEPTION_NOTICE = "matrix.exception.notice";

    /**
     * swagger配置
     */
    String SWAGGER = "matrix.swagger";

    /**
     * 版本负载均衡策略
     */
    String CONFIG_LOADBALANCE_ISOLATION = "matrix.load-balance.gray";

    String CONFIG_LOADBALANCE_ISOLATION_DEFAULT_VERSION = "matrix.load-balance.gray.defaultVersion";

    String CONFIG_LOADBALANCE_ISOLATION_CHOOSER = CONFIG_LOADBALANCE_ISOLATION + ".chooser";

}
