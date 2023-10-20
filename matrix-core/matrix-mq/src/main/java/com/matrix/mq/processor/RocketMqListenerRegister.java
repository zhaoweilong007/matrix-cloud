package com.matrix.mq.processor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.matrix.auto.properties.RocketMQProperties;
import com.matrix.mq.annotation.RocketMQMessageListener;
import com.matrix.mq.constans.MQConstant;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消费者监听器注册
 *
 * @author ZhaoWeiLong
 * @since 2023/3/23
 **/
@RequiredArgsConstructor
@Slf4j
public class RocketMqListenerRegister implements ApplicationContextAware {

    private final RocketMQProperties rocketMQProperties;
    private final List<Consumer> consumers = new ArrayList<>();
    private final Map<String, Consumer> consumerBeanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<Properties> consumers = rocketMQProperties.getConsumers();
        if (CollectionUtils.isEmpty(consumers)) {
            return;
        }
        log.info("register rocketmq consumer init");

        consumers.forEach(this::registerConsumer);

        final Map<String, MessageListener> beansOfType = applicationContext.getBeansOfType(MessageListener.class);
        Map<String, List<Object>> annotationMap = beansOfType.values().stream().filter(messageListener -> {
            Class<?> clazz = AopUtils.getTargetClass(messageListener);
            RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
            return annotation != null;
        }).collect(Collectors.groupingBy(bean -> {
            Class<?> clazz = AopUtils.getTargetClass(bean);
            RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
            if (StrUtil.isEmpty(annotation.consumerGroup())) {
                throw new IllegalArgumentException("consumerGroup can not be null");
            }
            if (ArrayUtil.isEmpty(annotation.tag())) {
                throw new IllegalArgumentException("tag can not be null");
            }
            return annotation.consumerGroup();
        }));

        //注册监听器
        annotationMap.forEach((group, beans) -> {
            Consumer consumer = consumerBeanMap.get(buildBeanName(group));
            if (consumer != null) {
                beans.forEach(object -> {
                    Class<?> clazz = AopUtils.getTargetClass(object);
                    RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
                    String subExpression = StringUtils.arrayToDelimitedString(annotation.tag(), " || ");
                    consumer.subscribe(annotation.topic(), subExpression, ((MessageListener) object));
                });
            }
        });
    }

    @PostConstruct
    public void start() {
        consumers.forEach(Consumer::start);
    }

    @PreDestroy
    public void destroy() {
        consumers.forEach(Consumer::shutdown);
    }

    private void registerConsumer(Properties properties) {
        String groupId = properties.getProperty("groupId");
        final Consumer consumer = ONSFactory.createConsumer(properties);
        consumers.add(consumer);
        consumerBeanMap.put(buildBeanName(groupId), consumer);
    }

    private String buildBeanName(String groupId) {
        return MQConstant.CONSUMER_PREFIX + groupId;
    }


}
