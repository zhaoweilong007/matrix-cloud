package com.matrix.redis.config.properties;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson 配置属性
 */
@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    /**
     * redis缓存key前缀
     */
    private String keyPrefix;

    /**
     * 线程池数量,默认值 = 当前处理核数量 * 2
     */
    private int threads;

    /**
     * Netty线程池数量,默认值 = 当前处理核数量 * 2
     */
    private int nettyThreads;

    /**
     * 单机服务配置
     */
    private SingleServerConfig singleServerConfig;

    /**
     * 主从服务配置
     */
    private MasterSlaveConfig masterSlaveServersConfig;

    /**
     * 集群服务配置
     */
    private ClusterConfig clusterServersConfig;

    @Data
    @NoArgsConstructor
    public static class SingleServerConfig {

        private Boolean enable = false;

        /**
         * redis地址
         */
        private String address;

        /**
         * db
         */
        private Integer database;

        /**
         * 密码
         */
        private String password;

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * 最小空闲连接数
         */
        private Integer connectionMinimumIdleSize;

        /**
         * 连接池大小
         */
        private Integer connectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒
         */
        private Integer idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒
         */
        private Integer timeout;

        /**
         * 发布和订阅连接池大小
         */
        private Integer subscriptionConnectionPoolSize;
    }

    @Data
    @NoArgsConstructor
    public static class MasterSlaveConfig {

        private Boolean enable = false;

        /**
         * 从节点地点
         */
        private Set<String> slaveAddresses = new HashSet<>();
        /**
         * 主节点地址
         */
        private String masterAddress;

        /**
         * db
         */
        private Integer database = 0;

        /**
         * 密码
         */
        private String password;

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * master最小空闲连接数
         */
        private Integer masterConnectionMinimumIdleSize;

        /**
         * master连接池大小
         */
        private Integer masterConnectionPoolSize;

        /**
         * slave最小空闲连接数
         */
        private Integer slaveConnectionMinimumIdleSize;

        /**
         * slave连接池大小
         */
        private Integer slaveConnectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒
         */
        private Integer idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒
         */
        private Integer timeout;

        /**
         * 发布和订阅连接池大小
         */
        private Integer subscriptionConnectionPoolSize;

        /**
         * 读取模式
         */
        private ReadMode readMode;

        /**
         * 订阅模式
         */
        private SubscriptionMode subscriptionMode;
    }

    @Data
    @NoArgsConstructor
    public static class ClusterConfig {

        private Boolean enable = false;

        /**
         * db
         */
        private Integer database = 0;

        /**
         * 密码
         */
        private String password;

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * master最小空闲连接数
         */
        private Integer masterConnectionMinimumIdleSize;

        /**
         * master连接池大小
         */
        private Integer masterConnectionPoolSize;

        /**
         * slave最小空闲连接数
         */
        private Integer slaveConnectionMinimumIdleSize;

        /**
         * slave连接池大小
         */
        private Integer slaveConnectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒
         */
        private Integer idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒
         */
        private Integer timeout;

        /**
         * 发布和订阅连接池大小
         */
        private Integer subscriptionConnectionPoolSize;

        /**
         * 读取模式
         */
        private ReadMode readMode;

        /**
         * 订阅模式
         */
        private SubscriptionMode subscriptionMode;
    }
}
