package com.matrix.mq.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/24
 **/
@Slf4j
public class MessageHandlerManager {

    private final Map<String, MessageHandler<?>> handlerMap = new ConcurrentHashMap<>();
    private final Map<MessageHandler<?>, Class<?>> classMap = new ConcurrentHashMap<>();
    @Autowired(required = false)
    private List<MessageHandler<?>> messageHandler;

    @PostConstruct
    public void init() {
        if (CollUtil.isEmpty(messageHandler)) {
            return;
        }
        messageHandler.forEach(h -> {
            final String[] tag = h.tag();
            for (String t : tag) {
                handlerMap.put(h.consumerGroup() + "-" + h.topic() + "-" + t, h);
            }
            final Class<?> handlerType = ClassUtil.getTypeArgument(h.getClass(), 0);
            classMap.put(h, handlerType);
        });
    }

    public Class<?> getHandlerType(MessageHandler<?> handler) {
        return classMap.get(handler);
    }


    public MessageHandler<?> getHandler(String consumerGroup, String topic, String tag) {
        if (handlerMap.isEmpty()) {
            return null;
        }
        return handlerMap.get(consumerGroup + "-" + topic + "-" + tag);

    }

}
