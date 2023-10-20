package com.matrix.feign.chooser;

import cn.hutool.core.util.StrUtil;
import com.matrix.auto.properties.GaryLoadBalanceProperties;
import com.matrix.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 多人协作负载均衡隔离
 *
 * @author ZhaoWeiLong
 * @since 2023/6/19
 **/
@Slf4j
public class ProfileRuleChooser implements IRuleChooser {

    private final GaryLoadBalanceProperties loadBalanceProperties = SpringUtils.getBean(GaryLoadBalanceProperties.class);

    private final Environment environment = SpringUtils.getBean(Environment.class);


    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        final String host = getHost();
        final Set<String> ips = loadBalanceProperties.getIps();
        for (ServiceInstance instance : instances) {
            final String instanceHost = instance.getHost();
            if (Objects.equals(instanceHost, host) || ips.contains(instanceHost)) {
                return instance;
            }
        }
        return null;
    }

    /**
     * 获取服务host
     * 默认自动获取
     */
    public String getHost() {
        String host = environment.getProperty("spring.cloud.nacos.discovery.ip");
        if (StrUtil.isNotBlank(host)) {
            return host;
        }
        try {
            host = "127.0.0.1";
            // 如需自定义ip可修改此处
            String address = InetAddress.getLocalHost().getHostAddress();
            if (address != null) {
                host = address;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }
}
