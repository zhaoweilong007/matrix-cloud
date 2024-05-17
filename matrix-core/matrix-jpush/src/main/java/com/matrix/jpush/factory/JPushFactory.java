package com.matrix.jpush.factory;

import com.matrix.common.push.enums.AppEnum;
import com.matrix.jpush.JPushTemplate;
import com.matrix.jpush.properties.JPushProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhaoWeiLong
 * @since 2024/1/3
 **/
public class JPushFactory {
    private final static Map<AppEnum, JPushTemplate> appMaps = new ConcurrentHashMap<>();

    private JPushFactory() {
    }

    public static void createTemplate(AppEnum appEnum, JPushProperties jPushProperties) {
        final JPushTemplate jPushTemplate = new JPushTemplate(jPushProperties);
        appMaps.put(appEnum, jPushTemplate);
    }


    public static JPushTemplate getTemplate(AppEnum appEnum) {
        return appMaps.get(appEnum);
    }
}
