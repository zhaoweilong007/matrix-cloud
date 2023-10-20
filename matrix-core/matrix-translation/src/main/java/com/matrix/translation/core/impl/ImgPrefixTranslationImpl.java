package com.matrix.translation.core.impl;

import cn.hutool.core.util.StrUtil;
import com.matrix.auto.properties.OssProperties;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 阿里云图片前缀
 *
 * @author ZhaoWeiLong
 * @since 2023/7/11
 **/
@RequiredArgsConstructor
@TranslationType(type = TransConstant.IMG_PREFIX_URL)
@Component
public class ImgPrefixTranslationImpl implements TranslationInterface<Object> {

    public static final String CO_DO = ";";
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

        if (value instanceof List) {
            String finalOther = other;
            return ((List<?>) value).stream().map(o -> mapping(o, finalOther)).collect(Collectors.toList());
        }


        if (value instanceof Map) {
            final String[] fields = translation.fields();
            final Map<String, Object> map = (Map) value;
            for (String field : fields) {
                if (map.containsKey(field)) {
                    Object val = map.get(field);
                    Object mappingValue = null;
                    if (val instanceof String) {
                        mappingValue = mapping(val, other);
                    } else if (val instanceof List<?>) {
                        String finalOther = other;
                        mappingValue = ((List<?>) val).stream().map(o -> mapping(o, finalOther)).collect(Collectors.toList());
                    }
                    map.put(field, mappingValue);
                }
            }
            return map;
        }


        return value;
    }

    private Object mapping(Object value, String other) {
        final String val = (String) value;
        //添加支持字符串为多个路径的且以逗号分隔的情况
        if (StrUtil.isBlank(val)) {
            return value;
        }
        return Arrays.stream(val.split(other))
                .map(url -> {
                    if (!StrUtil.contains(url, ossProperties.getPrefix())) {
                        return ossProperties.getPrefix() + url;
                    }
                    return url;
                }).collect(Collectors.joining(other));
    }
}
