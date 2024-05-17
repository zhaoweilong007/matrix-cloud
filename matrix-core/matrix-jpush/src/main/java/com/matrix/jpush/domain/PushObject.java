package com.matrix.jpush.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PushObject {

    Map<String, Object> extras = new HashMap<String, Object>();
    /**
     * 通知信息
     */
    private String alert;
    /**
     * 消息内容
     */
    private String msgContent;
    // ios声音
    private String sound = "happy";
    // ios右上角条数
    private int badge = 1;

}
