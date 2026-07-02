package com.matrix.jpush.factory;

import com.matrix.common.push.enums.AppEnum;
import com.matrix.jpush.JPushTemplate;
import com.matrix.jpush.properties.JPushProperties;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 极光推送客户端工厂，管理多应用 JPushTemplate 实例
 **/
public class JPushFactory {
    /**
     * 应用模板缓存
     */
    private static final Map<AppEnum, JPushTemplate> appMaps = new ConcurrentHashMap<>();

    private JPushFactory() {}

    /**
     * 创建并注册指定应用的 JPush 模板
     *
     * @param appEnum          应用枚举
     * @param jPushProperties  应用配置
     */
    public static void createTemplate(AppEnum appEnum, JPushProperties jPushProperties) {
        final JPushTemplate jPushTemplate = new JPushTemplate(jPushProperties);
        appMaps.put(appEnum, jPushTemplate);
    }

    /**
     * 获取指定应用的 JPush 模板
     *
     * @param appEnum 应用枚举
     * @return JPush 模板
     */
    public static JPushTemplate getTemplate(AppEnum appEnum) {
        return appMaps.get(appEnum);
    }
}
