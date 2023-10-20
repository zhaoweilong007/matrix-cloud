package com.matrix.translation.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.matrix.common.model.SysRegionDict;
import com.matrix.common.result.R;
import com.matrix.common.util.VUtils;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.api.client.IAreaNameService;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 行政区域翻译实现
 *
 * @author LeonZhou
 */
@RequiredArgsConstructor
@TranslationType(type = TransConstant.AREA_CODE_TO_NAME)
@Slf4j
@Component
public class AreaTranslationImpl implements TranslationInterface<Object> {

    public static final String AREA_CODE_PREFIX = "matrix_region_code:%s#30d";
    public static final String CO_DO = ";";
    private final IAreaNameService areaNameService;

    @Override
    public Object translation(Object key, Translation translation) {
        String other = translation.other();
        if (Objects.isNull(key)) {
            return null;
        }
        if (!String.class.isAssignableFrom(key.getClass())
                && !List.class.isAssignableFrom(key.getClass())) {
            log.warn("字段类型不支持转换：{}", key.getClass());
            return null;
        }
        if (StrUtil.isBlank(other)) {
            other = CO_DO;
        }
        String areaName = null;
        if (key instanceof String str) {
            areaName = mapping(str, other);
        }
        if (key instanceof List<?> list) {
            if (CollUtil.isEmpty(list)) {
                return null;
            }
            Map<String, String> result = new ConcurrentHashMap<>();
            String finalOther = other;
            list.parallelStream().map(o -> (String) o).forEach(s ->
                    result.put(s, mapping(s, finalOther))
            );
            return result;
        }

        return areaName;
    }

    public String mapping(String key, String other) {
        if (StrUtil.isBlank(key)) {
            return "";
        }
        if (StrUtil.isBlank(other)) {
            if (Validator.isChinese(key)) {
                return key;
            }
            final String regionKey = String.format(AREA_CODE_PREFIX, key);
            String regionName = RedisUtils.getCacheObject(regionKey);
            if (regionName != null) {
                return regionName;
            }

            R<String> res = areaNameService.selectRegionNameByCode(key);
            if (res != null && VUtils.checkRes(res)) {
                regionName = res.getData();
            }
            if (StrUtil.isNotBlank(regionName)) {
                RedisUtils.setCacheObject(regionKey, regionName);
            }
            return regionName;

        } else {
            final String[] areaCodes = key.trim().split(other);
            Set<String> regionCodes = Sets.newHashSet();
            Set<String> regionNames = Sets.newHashSet();
            Arrays.stream(areaCodes).forEach(code -> {
                if (Validator.isChinese(code)) {
                    regionNames.add(code);
                    return;
                }
                final String regionKey = String.format(AREA_CODE_PREFIX, code);
                final String regionName = RedisUtils.getCacheObject(regionKey);
                if (StrUtil.isBlank(regionName)) {
                    regionCodes.add(code);
                } else {
                    regionNames.add(regionName);
                }
            });
            if (CollUtil.isNotEmpty(regionCodes)) {
                final R<List<SysRegionDict>> r = areaNameService.selectRegionNameByCodes(Lists.newArrayList(regionCodes));
                if (VUtils.checkRes(r)) {
                    r.getData().forEach(region -> {
                        final String regionKey = String.format(AREA_CODE_PREFIX, region.getAreaCode());
                        RedisUtils.setCacheObject(regionKey, region.getAreaName());
                        regionNames.add(region.getAreaName());
                    });
                }
            }
            return String.join(other, regionNames);
        }
    }
}
