package com.matrix.translation.core.impl;

import cn.hutool.core.util.StrUtil;
import com.matrix.auto.properties.OssProperties;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 图片前缀翻译实现，为图片 URL 自动补全 OSS 前缀
 */
@RequiredArgsConstructor
@TranslationType(type = TransConstant.IMG_PREFIX_URL)
@Component
public class ImgPrefixTranslationImpl implements TranslationInterface<Object> {

    /**
     * 多图片 URL 分隔符
     */
    public static final String CO_DO = ";";
    /**
     * OSS 配置属性，用于获取图片前缀
     */
    private final OssProperties ossProperties;

    @Override
    public Object translation(Object value, Translation translation) {
        String other = translation.other();
        if (Objects.isNull(value)) {
            return null;
        }
        if (StrUtil.isBlank(other)) {
            other = CO_DO;
        }

        if (ossProperties == null || StrUtil.isBlank(ossProperties.getPrefix())) {
            return value;
        }

        if (value instanceof String) {
            return mapping(value, other);
        }

        if (value instanceof List<?> values) {
            String finalOther = other;
            return values.stream()
                    .map(item -> item instanceof String ? mapping(item, finalOther) : item)
                    .collect(Collectors.toList());
        }

        if (value instanceof Map<?, ?> source) {
            final String[] fields = translation.fields();
            final Map<Object, Object> map = new LinkedHashMap<>(source);
            for (String field : fields) {
                if (map.containsKey(field)) {
                    Object val = map.get(field);
                    Object mappingValue = val;
                    if (val instanceof String text) {
                        mappingValue = mapping(text, other);
                    } else if (val instanceof List<?> values) {
                        String finalOther = other;
                        mappingValue = values.stream()
                                .map(item -> item instanceof String ? mapping(item, finalOther) : item)
                                .collect(Collectors.toList());
                    }
                    map.put(field, mappingValue);
                }
            }
            return map;
        }

        return value;
    }

    /**
     * 为单个图片路径补全 OSS 前缀
     *
     * @param value 图片路径
     * @param other 分隔符
     * @return 补全前缀后的完整 URL
     */
    private Object mapping(Object value, String other) {
        final String val = (String) value;
        // 添加支持字符串为多个路径的且以逗号分隔的情况
        if (StrUtil.isBlank(val)) {
            return value;
        }
        return Arrays.stream(val.split(other))
                .map(url -> {
                    if (!StrUtil.contains(url, ossProperties.getPrefix())) {
                        return ossProperties.getPrefix() + url;
                    }
                    return url;
                })
                .collect(Collectors.joining(other));
    }
}
