package com.matrix.feign.chooser;

import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import java.util.List;
import org.springframework.cloud.client.ServiceInstance;

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
