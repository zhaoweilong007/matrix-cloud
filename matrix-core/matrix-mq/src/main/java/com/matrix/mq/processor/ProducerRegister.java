package com.matrix.mq.processor;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.matrix.auto.properties.RocketMQProperties;
import com.matrix.mq.producer.RocketMqTemplate;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 生产者注册配置
 *
 * @author ZhaoWeiLong
 * @since 2023/3/22
 **/
@RequiredArgsConstructor
@Slf4j
public class ProducerRegister implements BeanPostProcessor, ApplicationContextAware {

    private final RocketMQProperties rocketMQProperties;

    private final List<Producer> producers = new ArrayList<>();

    private DefaultListableBeanFactory beanFactory;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        List<Properties> producers = rocketMQProperties.getProducers();
        if (CollectionUtils.isEmpty(producers)) {
            return;
        }
        log.info("register rocketmq producer init");
        producers.forEach(properties -> {
            String groupId = properties.getProperty("groupId");
            registerRocketMqTemplate(properties, groupId);
        });
    }

    @PostConstruct
    public void init() {
        producers.forEach(Producer::start);
    }

    @PreDestroy
    public void destroy() {
        producers.forEach(Producer::shutdown);
    }

    private void registerRocketMqTemplate(Properties properties, String groupId) {
        final Producer producer = ONSFactory.createProducer(properties);
        final RocketMqTemplate rocketMqTemplate = new RocketMqTemplate();
        rocketMqTemplate.setProducer(producer);
        beanFactory.registerSingleton(groupId, rocketMqTemplate);
        producers.add(producer);
    }

}
