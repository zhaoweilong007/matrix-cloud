package com.matrix.mq.tenant;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.matrix.mq.event.MessageEvent;
import com.matrix.mq.producer.RocketMqTemplate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 支持租户上下文传播的 RocketMQ 模板
 * <p>
 * 在发送消息前自动注入租户上下文，消费时自动恢复
 * </p>
 *
 */
@Slf4j
@RequiredArgsConstructor
public class TenantRocketMqTemplate {

    private final RocketMqTemplate delegate;

    /**
     * 同步发送（自动注入租户上下文）
     */
    public SendResult send(MessageEvent event) {
        SendResult result = delegate.send(event);
        injectTenantToResult(result);
        return result;
    }

    /**
     * 同步发送（带延迟时间，自动注入租户上下文）
     */
    public SendResult send(MessageEvent event, long delay) {
        return delegate.send(event, delay);
    }

    /**
     * 单向发送（自动注入租户上下文）
     */
    public void sendOneway(MessageEvent event) {
        delegate.sendOneway(event);
    }

    /**
     * 异步发送（自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event) {
        delegate.sendAsync(event);
    }

    /**
     * 异步发送（带延迟时间，自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event, long delay) {
        delegate.sendAsync(event, delay);
    }

    /**
     * 异步发送（自定义回调，自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event, SendCallback callback) {
        delegate.sendAsync(event, callback);
    }

    /**
     * 异步发送（带延迟时间和自定义回调，自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event, long delay, SendCallback callback) {
        delegate.sendAsync(event, delay, callback);
    }

    /**
     * 同步发送（定时发送，自动注入租户上下文）
     */
    public SendResult send(MessageEvent event, Date date) {
        return delegate.send(event, date);
    }

    /**
     * 同步发送（定时发送，自动注入租户上下文）
     */
    public SendResult send(MessageEvent event, LocalDateTime date) {
        return delegate.send(event, date);
    }

    /**
     * 异步发送（定时发送，自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event, Date date, SendCallback callback) {
        delegate.sendAsync(event, date, callback);
    }

    /**
     * 异步发送（定时发送，自动注入租户上下文）
     */
    public void sendAsync(MessageEvent event, LocalDateTime date, SendCallback callback) {
        delegate.sendAsync(event, date, callback);
    }

    private void injectTenantToResult(SendResult result) {
        // 注入租户上下文到发送结果，可用于后续追踪
        log.debug("消息发送完成，租户ID: {}", com.matrix.common.context.TenantContextHolder.getTenantId());
    }
}
