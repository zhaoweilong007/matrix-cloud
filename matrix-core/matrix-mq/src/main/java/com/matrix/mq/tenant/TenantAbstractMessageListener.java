package com.matrix.mq.tenant;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.matrix.mq.annotation.RocketMQMessageListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 支持租户上下文传播的消息监听器
 * <p>
 * 在消费消息前自动恢复租户上下文，消费完成后清理
 * </p>
 *
 * @param <T> 消息体类型
 * @author matrix
 */
@Slf4j
public abstract class TenantAbstractMessageListener<T> implements MessageListener {

    public Class<T> clazz = (Class<T>) ClassUtil.getTypeArgument(this.getClass(), 0);
    public RocketMQMessageListener annotation = this.getClass().getAnnotation(RocketMQMessageListener.class);

    /**
     * 处理消息（子类实现）
     *
     * @param body 消息体
     */
    public abstract void handle(T body);

    @Override
    public Action consume(Message message, ConsumeContext context) {
        try {
            // 恢复租户上下文
            TenantMqUtils.restoreTenantContext(message);
            log.debug("消费消息前恢复租户上下文: tenantId={}",
                    com.matrix.common.context.TenantContextHolder.getTenantId());

            // 处理消息
            handle(JSON.parseObject(message.getBody(), clazz));
            return Action.CommitMessage;
        } catch (Exception e) {
            // 消费失败
            log.warn(
                    "handle message fail consumerGroup:【{}】 topic:【{}】,tag:【{}】 msgId:【{}】",
                    annotation.consumerGroup(),
                    message.getTopic(),
                    message.getTag(),
                    message.getMsgID(),
                    e);
            return Action.ReconsumeLater;
        } finally {
            // 清理租户上下文
            TenantMqUtils.clearTenantContext();
            log.debug("消费消息后清理租户上下文");
        }
    }
}
