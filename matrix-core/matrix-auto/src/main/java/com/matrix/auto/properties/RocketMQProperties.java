package com.matrix.auto.properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.matrix.common.util.string.StringUtils;
import java.util.List;
import java.util.Properties;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

/**
 * rocketMq配置 支持多生产者、多消费者组
 *
 * @author ZhaoWeiLong
 * @since 2023/3/22
 */

@ConfigurationProperties(prefix = "matrix.mq")
@Data
public class RocketMQProperties implements InitializingBean {

    /**
     * 是否启用 RocketMQ
     */
    private Boolean enabled;

    /**
     * ONS/RocketMQ 服务地址
     */
    private String onsAddr;

    /**
     * RocketMQ 访问密钥
     */
    private String accessKey;

    /**
     * RocketMQ 秘密密钥
     */
    private String secretKey;


    /**
     * 生产者配置列表
     */
    private List<Properties> producers;

    /**
     * 消费者配置列表
     */
    private List<Properties> consumers;

    @Override
    public void afterPropertiesSet() {
        if (!enabled) {
            return;
        }
        if (StringUtils.isEmpty(onsAddr)) {
            throw new IllegalArgumentException("onsAddr can not be empty");
        }
        if (StringUtils.isEmpty(accessKey)) {
            throw new IllegalArgumentException("accessKey can not be empty");
        }
        if (StringUtils.isEmpty(secretKey)) {
            throw new IllegalArgumentException("secretKey can not be empty");
        }
        if (!CollectionUtils.isEmpty(producers)) {
            producers.stream().peek(this::checkProperties).forEach(this::initProperties);
        }
        if (!CollectionUtils.isEmpty(consumers)) {
            consumers.stream().peek(this::checkProperties).forEach(this::initProperties);
        }
    }


    private void checkProperties(Properties properties) {
        if (StringUtils.isEmpty(properties.getProperty("groupId"))) {
            throw new NullPointerException("groupId cannot be empty");
        }
    }

    private void initProperties(Properties properties) {
        properties.put(PropertyKeyConst.GROUP_ID, properties.getProperty("groupId"));
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        properties.put(PropertyKeyConst.NAMESRV_ADDR, onsAddr);
    }


}
