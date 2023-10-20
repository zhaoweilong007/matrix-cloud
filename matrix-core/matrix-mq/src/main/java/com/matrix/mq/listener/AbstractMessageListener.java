package com.matrix.mq.listener;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.matrix.mq.annotation.RocketMQMessageListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息监听者需要继承该抽象类，实现handle方法，消息消费逻辑处理(如果抛出异常，则重新入队列)
 */
@Slf4j
public abstract class AbstractMessageListener<T> implements MessageListener {

    public Class<T> clazz = (Class<T>) ClassUtil.getTypeArgument(this.getClass(), 0);
    public RocketMQMessageListener annotation = this.getClass().getAnnotation(RocketMQMessageListener.class);

    public abstract void handle(T body);

    @Override
    public Action consume(Message message, ConsumeContext context) {
        try {
            handle(JSON.parseObject(message.getBody(), clazz));
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            log.warn("handle message fail consumerGroup:【{}】 topic:【{}】,tag:【{}】 msgId:【{}】", annotation.consumerGroup(), message.getTopic(), message.getTag(), message.getMsgID(), e);
            return Action.ReconsumeLater;
        }
    }
}
