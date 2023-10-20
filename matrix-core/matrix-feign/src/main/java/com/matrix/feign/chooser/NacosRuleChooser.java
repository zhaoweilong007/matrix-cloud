package com.matrix.feign.chooser;

import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/18
 **/
public class NacosRuleChooser implements IRuleChooser {


    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        return NacosBalancer.getHostByRandomWeight3(instances);
    }
}
