package com.matrix.auto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "heduohao.middleno")
@Component
public class MiddleNoProperties {

    private String url;
    private String appid;
    private String devid;
    private String authKey;
    private List<String> numberList = new ArrayList<>();
    private Boolean imitateFlag = false;//是否模拟

}
