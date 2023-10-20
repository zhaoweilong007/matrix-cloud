package com.matrix.mq.producer;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.matrix.mq.event.MessageEvent;

/**
 * 用作LocalTransactionExecuteImpl的构造器参数,用于执行本地事务操作
 */
@FunctionalInterface
public interface TransactionChecker {

    TransactionStatus check(MessageEvent messageEvent, Long hashValue);
}
