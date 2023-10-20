package com.matrix.mq.constans;

/***
 *
 * @author LeonZhou
 * @since 2023/7/5
 */
public interface TagConstant {


    String TAG_FANGDX_BROKER_HOUSE_VISIT_ALERT = "matrix-tag-houseVisitAlert";

    /**
     * 房源审核结果通知
     */
    String HOUSE_AUDIT_ALERT = "matrix-tag-house_audit_alert";

    /**
     * 小区反馈
     */
    String HOUSE_FEED_BACK = "matrix-tag-house_feed_back";

    /**
     * websocket 单条通知
     */
    String WS_SINGLE_MSG = "matrix-tag-ws_single_msg";

    /**
     * 用户业务数据转入公池
     */
    String USER_BUS_DATA_TRANSFER = "matrix-tag-userBusDataTransfer";

    /**
     * 自主注册用户同步到客户列表
     */
    String TAG_PLATFORM_CUSTOMER_GEN = "matrix-tag-platformCustomerGen";

    /**
     * 系统消息通知
     */
    String SYSTEM_NOTIFY_MESSAGE = "matrix-tag-system_notify_message";
}
