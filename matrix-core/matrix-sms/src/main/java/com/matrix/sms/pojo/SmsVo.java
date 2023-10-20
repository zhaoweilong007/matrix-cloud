package com.matrix.sms.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class SmsVo {

    private String phone;//电话号码
    private String signName;//签名
    private String templateId;//模板ID
    private JSONObject params;//短信内容
}
