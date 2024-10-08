package com.matrix.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.matrix.mq.event.MessageEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 普通消息,定时消息,延迟消息生产者
 */
@Slf4j
public class RocketMqTemplate {

    @Setter
    private Producer producer;

    private Message getMessage(MessageEvent event) {
        if (event == null) {
            throw new RuntimeException("event is null.");
        }
        if (StrUtil.isEmpty(event.getTopic()) || null == event.getDomain()) {
            throw new RuntimeException("topic, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), JSON.toJSONBytes(event.getDomain()));
        message.setKey(event.generateTxId());
        return message;
    }

    /****
     * @Description: 同步发送
     * @Param: [event]
     */
    public SendResult send(MessageEvent event) {
        Message message = getMessage(event);
        SendResult result = this.producer.send(message);
        return result;
    }

    /****
     * @Description: 同步发送(带延迟时间)
     * @Param: [event, delay]
     */
    public SendResult send(MessageEvent event, long delay) {
        Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        SendResult result = this.producer.send(message);
        return result;
    }

    /**
     * @Description: 单向发送, 单向（Oneway）发送特点为发送方只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。 此方式发送消息的过程耗时非常短，一般在微秒级别。适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     * @Param: [event]
     */
    public void sendOneway(MessageEvent event) {
        Message message = getMessage(event);
        this.producer.sendOneway(message);
    }

    /**
     * @Description: 异步发送
     * @Param: [event]
     */
    public void sendAsync(MessageEvent event) {
        Message message = getMessage(event);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }


    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     */
    public void sendAsync(MessageEvent event, long delay) {
        Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }

    /**
     * @Description: 异步发送
     * @Param: [event]
     */
    public void sendAsync(MessageEvent event, SendCallback callback) {
        Message message = getMessage(event);
        this.producer.sendAsync(message, callback);
    }


    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     */
    public void sendAsync(MessageEvent event, long delay, SendCallback callback) {
        Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, callback);
    }

    /****
     * @Description: 同步发送(延迟定时发送, 请注意保证时间正确)
     * @Param: [event, delay]
     */
    public SendResult send(MessageEvent event, Date date) {
        long delay = getDelay(date);
        return send(event, delay);
    }

    private long getDelay(Date date) {
        Date now = new Date();
        long delay = date.getTime() - now.getTime();
        if (delay <= 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.warn("消息发送时间:" + sdf.format(date) + " 小于当前时间:" + sdf.format(now));
        }
        return delay;
    }

    /**
     * @Description: 异步发送(延迟定时发送, 请注意保证时间正确)
     * @Param: [event, delay]
     */
    public void sendAsync(MessageEvent event, Date date, SendCallback callback) {
        long delay = getDelay(date);
        sendAsync(event, delay, callback);
    }

    /****
     * @Description: 同步发送(延迟定时发送, 请注意保证时间正确)
     * @Param: [event, delay]
     */
    public SendResult send(MessageEvent event, LocalDateTime date) {
        long delay = getDelay(date);
        return send(event, delay);
    }

    private long getDelay(LocalDateTime date) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now();
        //时间间隔秒数
        long delay = date.atZone(zone).toInstant().getEpochSecond() - now.atZone(zone).toInstant().getEpochSecond();
        if (delay <= 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.warn("消息发送时间:" + sdf.format(date) + " 小于当前时间:" + sdf.format(now));
        }
        return delay * 1000;
    }

    /**
     * @Description: 异步发送(延迟定时发送, 请注意保证时间正确)
     * @Param: [event, delay]
     */
    public void sendAsync(MessageEvent event, LocalDateTime date, SendCallback callback) {
        long delay = getDelay(date);
        sendAsync(event, delay, callback);
    }
}
