package com.matrix.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/9/30 15:29
 **/
public class ProfileUtils {

    static List<String> profiles = Lists.newArrayList("test", "dev", "local");

    public static Boolean isTest() {
        List<String> activeProfile = StreamEx.of(SpringUtil.getActiveProfiles()).toList();
        return CollUtil.containsAny(activeProfile, profiles);
    }
}
