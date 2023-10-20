package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/23
 **/
@ConfigurationProperties(prefix = "yidun")
@Data
@RefreshScope
public class YiDunProperties {

    /**
     * 产品密钥id
     */
    private String secretId;

    /**
     * 产品密钥
     */
    private String secretKey;

    /**
     * 业务id
     */
    private String businessId;


    /**
     * img业务标识
     */
    private String imgBusinessId;


    /**
     * 文本检测回调地址
     */
    private String textCallBack;

    /**
     * 图片检测回调地址
     */
    private String imgCallBack;

    /**
     * 易盾文本数据前缀
     */
    private String dataIdPrefix;


}
