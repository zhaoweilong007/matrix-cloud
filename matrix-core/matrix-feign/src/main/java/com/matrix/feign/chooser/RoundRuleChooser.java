package com.matrix.feign.chooser;

import cn.hutool.core.collection.CollUtil;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

/**
 * 轮询选择器
 */
@Slf4j
public class RoundRuleChooser implements IRuleChooser {

    private final AtomicInteger position;

    public RoundRuleChooser() {
        this.position = new AtomicInteger(1000);
    }

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (CollUtil.isNotEmpty(instances)) {
            int index = Math.floorMod(position.getAndIncrement(), instances.size());
            ServiceInstance serviceInstance = instances.get(index);
            log.debug("选择了ip为{}, 端口为：{}的服务", serviceInstance.getHost(), serviceInstance.getPort());
            return serviceInstance;
        }
        return null;
    }
}
