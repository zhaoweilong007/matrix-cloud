package com.matrix.auto.properties;

import com.matrix.common.constant.ConfigConstants;
import com.matrix.common.constant.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;
import java.util.Set;

/**
 * 负载负载均衡配置
 *
 * @author ZhaoWeiLong
 * @since 2023/6/26
 **/
@Data
@ConfigurationProperties(ConfigConstants.CONFIG_LOADBALANCE_ISOLATION)
@RefreshScope
public class GaryLoadBalanceProperties {

    private Boolean enabled;

    /**
     * 自定义选择器
     */
    private String chooser;

    /**
     * 允许的ip
     */
    private Set<String> ips = new HashSet<>();

    /**
     * 默认版本号  未匹配版本号的情况下使用此版本号
     */
    private String defaultVersion = Constants.DEFAULT_VERSION;
}
