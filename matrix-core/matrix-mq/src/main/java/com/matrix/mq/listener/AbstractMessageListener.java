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
 * 消息监听者抽象类，子类实现 {@link #handle} 方法处理消息消费逻辑。
 *
 * <p>消费失败时返回 {@link Action#ReconsumeLater} 触发重试，
 * 超过最大重试次数（默认 16 次）后由 ONS/RocketMQ Broker
 * 自动转入死信队列 {@code %DLQ%<ConsumerGroup>}。</p>
 */
@Slf4j
public abstract class AbstractMessageListener<T> implements MessageListener {

    public Class<T> clazz = (Class<T>) ClassUtil.getTypeArgument(this.getClass(), 0);
    public RocketMQMessageListener annotation = this.getClass().getAnnotation(RocketMQMessageListener.class);

    /**
     * 处理消息，子类实现具体业务逻辑。
     *
     * @param body 消息体
     */
    public abstract void handle(T body);

    @Override
    public Action consume(Message message, ConsumeContext context) {
        try {
            handle(JSON.parseObject(message.getBody(), clazz));
            return Action.CommitMessage;
        } catch (Exception e) {
            log.warn("消息消费失败 consumerGroup:【{}】 topic:【{}】 tag:【{}】 msgId:【{}】 reconsumeTimes:【{}】",
                    annotation.consumerGroup(),
                    message.getTopic(),
                    message.getTag(),
                    message.getMsgID(),
                    message.getReconsumeTimes(),
                    e);
            return Action.ReconsumeLater;
        }
    }
}
