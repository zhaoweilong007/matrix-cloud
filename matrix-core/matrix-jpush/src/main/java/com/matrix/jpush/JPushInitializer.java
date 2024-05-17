package com.matrix.jpush;

import com.matrix.common.push.enums.AppEnum;
import com.matrix.jpush.factory.JPushFactory;
import com.matrix.jpush.properties.JPushProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author ZhaoWeiLong
 * @since 2024/1/3
 **/
@RequiredArgsConstructor
@Slf4j
public class JPushInitializer implements InitializingBean {


    private final Map<String, JPushProperties> jpushConfig;

    @Override
    public void afterPropertiesSet() {
        log.info("初始化极光推送");
        for (String appId : jpushConfig.keySet()) {
            final AppEnum appEnum = AppEnum.valueOf(appId);
            Assert.notNull(appEnum, "不支持该应用" + appId + ",请先配置后使用");
            final JPushProperties properties = jpushConfig.get(appId);
            Assert.notNull(properties, "未找到应用" + appId + "的配置信息");
            Assert.hasText(properties.getAppKey(), "未找到应用" + appId + "的AppKey");
            Assert.hasText(properties.getMasterSecret(), "未找到应用" + appId + "的MasterSecret");
            JPushFactory.createTemplate(appEnum, properties);
        }
    }


}

