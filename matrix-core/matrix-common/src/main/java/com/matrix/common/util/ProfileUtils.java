package com.matrix.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/9/30 15:29
 **/
@Slf4j
public class ProfileUtils {

    public static final List<String> profiles = Lists.newArrayList("test", "dev", "local");

    public static final List<String> activeProfile = StreamEx.of(SpringUtil.getActiveProfiles()).toList();

    public static final Boolean isTest = CollUtil.containsAny(activeProfile, profiles);

    public static Boolean isTest() {
        return isTest;
    }
}
