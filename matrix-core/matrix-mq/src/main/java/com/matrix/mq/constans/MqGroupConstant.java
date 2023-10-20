package com.matrix.mq.constans;

/**
 * mq分组
 *
 * @author ZhaoWeiLong
 * @since 2023/7/22
 **/
public interface MqGroupConstant {

    /**
     * 微信服务通知
     */
    String WX_MESSAGE_SUBSCRIBE = "GID_wx_message_subscribe";


    /**
     * 系统消息通知
     */
    String SYSTEM_NOTIFY_MESSAGE = "GID_system_notify_message";


    /**
     * webSocket订阅通知
     */
    String WS_NOTIFY_SUBSCRIBE = "GID_ws_notify_subscribe";

    /**
     * 小区反馈处理
     */
    String HOUSE_FEED_BACK = "GID_house_feed_back";

    /**
     * 用户相关消息
     */
    String SYS_USER = "GID_sys_user_data";

    /**
     * C端客户自主注册时，插入一条mq同步生成平台公客数据
     */
    String PLATFORM_CUSTOMER_GEN = "GID_platform_customer_generate";
}
