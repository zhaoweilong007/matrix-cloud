package com.matrix.mq.producer;

import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.matrix.mq.event.MessageEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 顺序消息生产者
 */
@Slf4j
public class OrderMessageTemplate {

    @Setter
    private OrderProducerBean orderProducer;

    /****
     * @Description: 同步发送顺序消息
     * @Param: [event]
     * @param: sharding 分区顺序消息中区分不同分区的关键字段，sharding key 于普通消息的 key 是完全不同的概念。
     */
    public SendResult send(MessageEvent event, String sharding) {
        if (event == null) {
            throw new RuntimeException("event is null.");
        }
        if (StringUtils.isEmpty(event.getTopic()) || null == event.getDomain()) {
            throw new RuntimeException("topic, or body is null");
        }
        Message message = new Message(event.getTopic(), event.getTag(), JSON.toJSONBytes(event.getDomain()));
        message.setKey(event.generateTxId());
        return this.orderProducer.send(message, sharding);
    }

    /****
     * @Description: 同步发送顺序消息
     * @Param: [event]
     * @param: sharding 分区顺序消息中区分不同分区的关键字段，sharding key 于普通消息的 key 是完全不同的概念。
     */
    public SendResult send(MessageEvent event) {
        return send(event, MessageOrderTypeEnum.GLOBAL);
    }

    /****
     * @Description: 同步发送顺序消息
     * @Param: [event]
     * @param: sharding 分区顺序消息中区分不同分区的关键字段，sharding key 于普通消息的 key 是完全不同的概念。
     */
    public SendResult send(MessageEvent event, MessageOrderTypeEnum orderType) {
        String sharding = "#global#";
        switch (orderType) {
            case GLOBAL:
                sharding = "#global#";
                break;
            case TOPIC:
                sharding = "#" + event.getTopic() + "#";
                break;
            case TAG:
                sharding = "#" + event.getTopic() + "#" + event.getTag() + "#";
                break;
        }
        return send(event, sharding);
    }
}
