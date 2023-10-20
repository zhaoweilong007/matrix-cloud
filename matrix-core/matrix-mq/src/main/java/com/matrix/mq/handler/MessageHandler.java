package com.matrix.mq.handler;

import com.matrix.mq.annotation.RocketMQMessageListener;

import java.lang.annotation.Annotation;

/**
 * 消息处理器 根据groupId-topic-tag处理消息类型
 *
 * @author ZhaoWeiLong
 * @since 2023/3/23
 **/
public interface MessageHandler<T> extends RocketMQMessageListener {

    void handler(T msg);

    @Override
    default Class<? extends Annotation> annotationType() {
        return RocketMQMessageListener.class;
    }
}
