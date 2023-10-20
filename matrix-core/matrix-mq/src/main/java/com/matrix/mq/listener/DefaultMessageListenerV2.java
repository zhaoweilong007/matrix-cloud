package com.matrix.mq.listener;

import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.matrix.mq.annotation.RocketMQMessageListener;
import com.matrix.mq.handler.MessageHandler;
import com.matrix.mq.handler.MessageHandlerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息默认处理器 按照groupId-topic-tag处理
 *
 * @author ZhaoWeiLong
 * @since 2023/3/23
 **/
@Slf4j
public class DefaultMessageListenerV2 implements MessageListener {


    public RocketMQMessageListener annotation = this.getClass().getAnnotation(RocketMQMessageListener.class);
    @Autowired
    private MessageHandlerManager messageHandlerManager;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        final MessageHandler messageHandler = messageHandlerManager.getHandler(annotation.consumerGroup(), message.getTopic(), message.getTag());
        if (messageHandler == null) {
            log.warn("msg handler fail not found MessageHandler consumerGroup:【{}】 topic:【{}】,tag:【{}】", annotation.consumerGroup(), message.getTopic(), message.getTag());
            return Action.ReconsumeLater;
        }
        try {
            final Class<?> handlerType = messageHandlerManager.getHandlerType(messageHandler);
            messageHandler.handler(JSON.parseObject(message.getBody(), handlerType));
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            log.warn("handle message fail consumerGroup:【{}】 topic:【{}】,tag:【{}】 msgId:【{}】", annotation.consumerGroup(), message.getTopic(), message.getTag(), message.getMsgID(), e);
            return Action.ReconsumeLater;
        }
    }
}
