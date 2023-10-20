package com.matrix.mq;

import com.matrix.auto.properties.RocketMQProperties;
import com.matrix.mq.handler.MessageHandlerManager;
import com.matrix.mq.processor.ProducerRegister;
import com.matrix.mq.processor.RocketMqListenerRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * rocketMq自动配置
 *
 * @author ZhaoWeiLong
 * @since 2023/3/22
 */
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnProperty(prefix = "matrix.mq", value = "enabled", havingValue = "true")
@AutoConfiguration
public class RocketMqAutoConfiguration {

    @Autowired
    private RocketMQProperties propConfig;


    @Bean
    public ProducerRegister producerRegister() {
        return new ProducerRegister(propConfig);
    }

    @Bean
    public RocketMqListenerRegister rocketMqListenerRegister() {
        return new RocketMqListenerRegister(propConfig);
    }

    @Bean
    public MessageHandlerManager messageHandlerManager() {
        return new MessageHandlerManager();
    }
}
