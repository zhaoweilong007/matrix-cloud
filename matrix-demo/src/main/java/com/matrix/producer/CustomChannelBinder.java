package com.matrix.producer;

import com.alibaba.fastjson2.JSON;
import com.matrix.consumer.DemoMessage;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/10 16:50
 **/
@Component
public class CustomChannelBinder {
    @Autowired
    private StreamBridge streamBridge;

    public void streamTestMsg(String msg, Integer i) {
        // 构建消息对象
        DemoMessage testMessaging = new DemoMessage()
                .setMsgId(UUID.randomUUID().toString())
                .setMsg(msg);

        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(testMessaging))
                .setHeader(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i)
                //设置延时等级1~10
                //.setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,2)
                //设置tag
                //.setHeader(MessageConst.PROPERTY_KEYS, "demo")
                .build();

        streamBridge.send("producer-out-0", message);
    }

}
