package com.matrix.auto.properties;

import com.matrix.common.constant.ConfigConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 配置alertmanager告警地址
 */
@Data
@ConfigurationProperties(prefix = ConfigConstants.EXCEPTION_NOTICE)
public class ExceptionNoticeProperties {

    public static final String MSG_TEMPLATE =
            """
                    [
                        {
                            "labels": {
                                "alertName": "温馨提示：服务名称:【%s】接口名称:【%s】存在异常！请尽快处理！"
                            },
                            "annotations": {
                                "timestamp": "%s",
                                "traceId": "%s",
                                "message": "%s" ,
                                "stackTrace": "%s"
                            }
                        }
                    ]""";
    private String alertUrl;
}