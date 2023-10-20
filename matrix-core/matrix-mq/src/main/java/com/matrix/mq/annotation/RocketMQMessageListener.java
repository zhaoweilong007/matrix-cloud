/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matrix.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息者监听(订阅消费内容)
 * <p>
 * 注意groupId、topic、tag的使用
 * <p>
 * groupId: 同一服务的不同实例公用同一个groupId
 * <p>
 * topic：业务场景分类，如order、notice、push分类等
 * <p>
 * tag：具体分类下的标签，如order下create、pay等细分场景
 *
 * @author ZhaoWeiLong
 * @since 2023/3/22
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQMessageListener {

    /**
     * 消费组
     */
    String consumerGroup();

    /**
     * 同一group下topic只能有一个,有多个的情况下按照注册优先级只能拿到一个
     * <p>
     * Topic name
     */
    String topic();

    /**
     * tag name
     */
    String[] tag();

}
