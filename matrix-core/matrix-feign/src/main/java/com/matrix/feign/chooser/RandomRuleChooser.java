package com.matrix.feign.chooser;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机的选择器
 */
@Slf4j
public class RandomRuleChooser implements IRuleChooser {
    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (CollUtil.isNotEmpty(instances)) {
            int randomValue = ThreadLocalRandom.current().nextInt(instances.size());
            ServiceInstance serviceInstance = instances.get(randomValue);
            log.info("选择了ip为{}, 端口为：{}的服务", serviceInstance.getHost(), serviceInstance.getPort());
            return serviceInstance;
        }
        return null;
    }
}
