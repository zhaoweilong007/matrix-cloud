package com.matrix.mq.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息处理器管理器，按 groupId-topic-tag 匹配并分发消息
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

    /**
     * 获取处理器对应的消息类型
     */
    public Class<?> getHandlerType(MessageHandler<?> handler) {
        return classMap.get(handler);
    }

    /**
     * 根据消费组、主题和标签获取对应的消息处理器
     */
    public MessageHandler<?> getHandler(String consumerGroup, String topic, String tag) {
        if (handlerMap.isEmpty()) {
            return null;
        }
        return handlerMap.get(consumerGroup + "-" + topic + "-" + tag);
    }
}
