package com.matrix.feign.chooser;

import java.util.List;
import org.springframework.cloud.client.ServiceInstance;

/**
 * service选择器类
 */
public interface IRuleChooser {
    ServiceInstance choose(List<ServiceInstance> instances);
}
