package com.matrix.mq.event;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

/**
 * MQ统一事件对象，用在跨业务整合中
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageEvent implements Serializable {

    /**
     * 事件创建时间
     */
    private final long createdDate = System.currentTimeMillis();
    /**
     * 事件序列ID
     */
    private String txId;
    /**
     * 话题的名字
     */
    private String topic;
    /**
     * 话题的名字
     */
    private String tag;
    /**
     * 需要传递的领域对象
     */
    private Object domain;
    /**
     * 传递的领域对象的唯一标识,用来构建消息的唯一标识,不检测重复,可以为空,不影响消息收发
     */
    private String domainKey;

    /**
     * 方便的生成TxId的方法
     *
     * @return
     */
    public String generateTxId() {
        if (null == txId) {
            txId = getTopic() + ":" + getTag() + ":";
            if (StringUtil.isNullOrEmpty(domainKey)) {
                txId = txId + getCreatedDate() + ":" + UUID.randomUUID();
            } else {
                txId = txId + domainKey;
            }
        }
        return txId;
    }

}
