package com.matrix.jpush.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * 极光推送消息对象，封装推送内容、附加参数等
 */
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
