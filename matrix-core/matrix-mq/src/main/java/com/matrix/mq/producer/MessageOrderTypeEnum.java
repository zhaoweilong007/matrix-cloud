package com.matrix.mq.producer;

/**
 * 顺序消息,消息分区类型
 */
public enum MessageOrderTypeEnum {

    /**
     * 全局顺序消息
     */
    GLOBAL((byte) 1),
    /**
     * topic作为分区的顺序消息,即同一topic消息保证为顺序消息
     */
    TOPIC((byte) 2),
    /**
     * topic 和tag 作为分区的顺序消息,即同一topic和tag消息保证为顺序消息
     */
    TAG((byte) 3);


    private byte code;

    MessageOrderTypeEnum(byte code) {
        this.code = code;
    }

    public static MessageOrderTypeEnum valueOf(byte code) {
        for (MessageOrderTypeEnum item : MessageOrderTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}
