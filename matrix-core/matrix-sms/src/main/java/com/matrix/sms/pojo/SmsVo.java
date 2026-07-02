package com.matrix.sms.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * 短信发送请求 VO。
 *
 * <p>封装短信发送所需的电话号码、签名、模板 ID 和模板参数。</p>
 */
@Data
public class SmsVo {

    private String phone; // 电话号码
    private String signName; // 签名
    private String templateId; // 模板ID
    private JSONObject params; // 短信内容
}
