package com.matrix.common.constant;

/**
 * 配置中心常量定义
 */
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

    /**
     * 负载均衡隔离默认版本配置 key
     */
    String CONFIG_LOADBALANCE_ISOLATION_DEFAULT_VERSION = "matrix.load-balance.gray.defaultVersion";

    /**
     * 负载均衡隔离策略选择器配置 key
     */
    String CONFIG_LOADBALANCE_ISOLATION_CHOOSER = CONFIG_LOADBALANCE_ISOLATION + ".chooser";

}
