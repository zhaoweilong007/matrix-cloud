package com.matrix.resource.config;

import com.matrix.resource.properties.OcrProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Configuration
@EnableConfigurationProperties(OcrProperties.class)
public class ResourceConfig {


    @Bean
    @ConditionalOnProperty(prefix = "matrix.ocr", name = "enabled", havingValue = "true")
    public OcrClient ocrClient(OcrProperties ocrProperties) {
        Credential cred = new Credential(ocrProperties.getSecretId(), ocrProperties.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");
        httpProfile.setConnTimeout(3000);
        httpProfile.setReadTimeout(5000);
        httpProfile.setWriteTimeout(3000);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new OcrClient(cred, "ap-guangzhou", clientProfile);
    }

}
